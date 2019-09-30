package com.ironelder.mykotlindemo.component

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ironelder.mykotlindemo.common.*
import com.ironelder.mykotlindemo.dao.DataVo
import com.ironelder.mykotlindemo.dao.DocumentDataVo
import com.ironelder.mykotlindemo.ui.CustomCardView
import com.ironelder.mykotlindemo.ui.FilterView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.app.Activity
import androidx.core.view.size
import com.ironelder.mykotlindemo.R
import com.ironelder.mykotlindemo.presenter.SearchContract
import com.ironelder.mykotlindemo.presenter.SearchPresenter


class MainActivity : AppCompatActivity(),SearchContract.searchViewContract {
    override fun searchResults(searchResult: List<DocumentDataVo>, isEnd: Boolean, type:String) {
        if(type == "blog"){
            println("blog")
            mIsBlogDataEnd = isEnd
            mBlogPage++
        }
        if(type == "cafe"){
            println("cafe")
            mIsCafeDataEnd = isEnd
            mCafePage++
        }

        mItemArrayList.addAll(searchResult)
        mRecyclerView.adapter?.notifyDataSetChanged()
    }

    override fun searchError() {
    }

    override fun searchError(errMsg: String) {
    }

    private lateinit var mSearchEditText:EditText
    private lateinit var mSearchButton:Button
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mOriginItemArrayList:ArrayList<DocumentDataVo>
    private lateinit var mItemArrayList:ArrayList<DocumentDataVo>
    private lateinit var mSearchHistorys:ArrayList<String>
    private lateinit var mSearchHistoryListView: ListView

    private var mIsBlogDataEnd = false
    private var mIsCafeDataEnd = false
    private var mBlogPage = 1
    private var mCafePage = 1
    private var mQueryString = ""
    private lateinit var mFilterBar : FilterView
    private var mFilterType:String = "All"
    private var mSortType:Int = SORT_TYPE_TITLE_ASC

    private lateinit var mSearchPresenter:SearchPresenter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var searchForKakaoRepo:SearchForKakaoRepo = SearchForKakaoRepo(RetrofitForKakao.getSearchForKakaoService())
        mSearchPresenter = SearchPresenter(this, searchForKakaoRepo)


        mOriginItemArrayList = ArrayList()
        mItemArrayList = ArrayList()
        mSearchHistorys = SharedPreferenceAPI.getStringArrayPref(applicationContext, "searchHistory")
        mSearchHistoryListView = findViewById(R.id.searchHistory)
        mSearchHistoryListView.adapter = ArrayAdapter(this@MainActivity, android.R.layout.simple_list_item_1, mSearchHistorys)
        mSearchHistoryListView.setOnItemClickListener { parent, view, position, id ->
            mQueryString = (parent as ListView).getItemAtPosition(position) as String
            mSearchEditText.setText(mQueryString)
            setSearchInit()
            mSearchEditText.clearFocus()
            callRetrofitService()
            hideKeybaord(view)
        }

        mFilterBar = findViewById(R.id.filterBar)
        mFilterBar.setFilterActionListener(object : FilterView.FilterActionListener{
            override fun onSelectedItem(selectItem: String) {
                if(mFilterType != selectItem){
                    mFilterType = selectItem
                    setSearchInit()
                    callRetrofitService()
                }
//                (mRecyclerView.adapter as CustomRecyclerAdapter).setFilter()
            }

            override fun onSortButtonClick() {
                var sortDlg = SortingDialog(this@MainActivity, mSortType)
                sortDlg.setDialogActionListener(
                    object : SortingDialog.DialogActionListener{
                        override fun setSortOrder(sortType: Int) {
                            if(sortType != mSortType){
                                mSortType = sortType
                                println(sortType)
                                println(mItemArrayList.get(0).title)
                                mItemArrayList = if(sortType == SORT_TYPE_TITLE_ASC){
                                    ArrayList(mItemArrayList.sortedBy { it.title })
                                }else{
                                    ArrayList(mItemArrayList.sortedByDescending { it.datetime })
                                }
                                mRecyclerView.adapter?.notifyDataSetChanged()
                                println(mItemArrayList.get(0).title)
                                println(mItemArrayList.size)
                            }
                            sortDlg.dismiss()
                        }
                    }
                )
                sortDlg.show()
            }
        })

        mRecyclerView = findViewById(R.id.recyclerView)
        mRecyclerView.adapter = CustomRecyclerAdapter()
        mRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mRecyclerView.setHasFixedSize(true)
        mRecyclerView.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager(this).orientation))
        addRecyclerViewScrollListener()

        mSearchEditText = findViewById(R.id.search_view)
        mSearchEditText.setOnFocusChangeListener { v, hasFocus ->
            if(hasFocus){
                mSearchHistoryListView.visibility = View.VISIBLE
            } else {
                mSearchHistoryListView.visibility = View.GONE
            }
        }
        mSearchButton = findViewById(R.id.search_btn)
        mSearchButton.setOnClickListener { view ->
            setSearchInit()
            mSearchEditText.clearFocus()
            mQueryString = mSearchEditText.text.toString()
            if(!mSearchHistorys.contains(mQueryString)){
                mSearchHistorys.add(mQueryString)
                SharedPreferenceAPI.setStringArrayPref(applicationContext, "searchHistory", mSearchHistorys)
            }
            callRetrofitService()
            hideKeybaord(view)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                200 -> {
                    var position = data?.getIntExtra("Position", 0)?:0
                    mItemArrayList[position].isRead = true
                    mRecyclerView.adapter?.notifyItemChanged(position+1)
                }
            }
        }
    }

    private fun addRecyclerViewScrollListener(){
        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView:RecyclerView, dx:Int, dy:Int){
                super.onScrolled(recyclerView, dx, dy)

                if (dy > 0) {
                    mFilterBar.visibility = View.GONE
                } else if (dy < 0) {
                    mFilterBar.visibility = View.VISIBLE
                }

                if(mRecyclerView.canScrollVertically(1)){
                    callRetrofitService()
                }
            }
        })
    }

    private fun setSearchInit(){
        mIsBlogDataEnd = false
        mIsCafeDataEnd = false
        mBlogPage = 1
        mCafePage = 1
        mItemArrayList.clear()
        mFilterBar.visibility = View.VISIBLE
    }

    private fun callRetrofitService(){
        if(!mIsBlogDataEnd && (mFilterType.contains("All")||mFilterType.contains("Blog"))){
            mSearchPresenter.searchForKakao("blog",mBlogPage,mQueryString)
        }
        if(!mIsCafeDataEnd && (mFilterType.contains("All")||mFilterType.contains("Cafe"))) {
            mSearchPresenter.searchForKakao("cafe",mCafePage,mQueryString)
        }
    }


    private fun hideKeybaord(v: View) {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(v.applicationWindowToken, 0)
    }

    private inner class CustomRecyclerAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            var view:View
            var holder:RecyclerView.ViewHolder
            if(viewType == 0){
                view = LayoutInflater.from(parent!!.context).inflate(R.layout.hearderview, parent, false);
                view.layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT)
                holder = HeaderViewHolder(view);
            } else {
                view = CustomCardView(parent!!.context)
                view.layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT)
                holder = CustomItemHolder(view)

            }

            return holder
        }

        override fun getItemViewType(position: Int): Int {
            return if(position == 0){
                VIEW_TYPE_HEADER
            } else {
                VIEW_TYPE_NORMAL
            }
        }

        override fun getItemCount(): Int {
            return mItemArrayList?.size + 1
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if(holder is CustomItemHolder) {
                var dataDto = mItemArrayList[position-1]
                holder.setData(dataDto)
                holder.setViewPosition(position-1)
                holder.setActionListener(object : CustomCardView.CustomActionListener{
                    override fun onClickItem(itemPosition: Int, itemId: Int) {
                        var detailIntent:Intent = Intent(this@MainActivity, DetailActivity::class.java)
                        detailIntent.putExtra("detailData", dataDto)
                        detailIntent.putExtra("Position", itemPosition)
                        detailIntent.putExtra("ItemId", itemId)
                        startActivityForResult(detailIntent, 200)
                    }
                })
            } else {
                val headerViewHolder = holder as HeaderViewHolder
            }
        }

        inner class CustomItemHolder: RecyclerView.ViewHolder{
            private val holderCustomListItem:CustomCardView
            constructor(view: View):super(view){
                holderCustomListItem = view as CustomCardView
            }
            fun setData(dataDto: DocumentDataVo){
                holderCustomListItem.setData(dataDto)
            }
            fun setViewPosition(p:Int){
                holderCustomListItem.setPosition(p)
            }
            fun setActionListener(l:CustomCardView.CustomActionListener){
                holderCustomListItem.setCustomActionListener(l)
            }
        }
        inner class HeaderViewHolder:RecyclerView.ViewHolder{
            constructor(headerView:View):super(headerView)
        }
    }
}
