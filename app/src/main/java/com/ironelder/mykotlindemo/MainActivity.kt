package com.ironelder.mykotlindemo

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import androidx.annotation.NonNull
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ironelder.mykotlindemo.common.*
import com.ironelder.mykotlindemo.component.SortingDialog
import com.ironelder.mykotlindemo.dao.DataVo
import com.ironelder.mykotlindemo.dao.DocumentDataVo
import com.ironelder.mykotlindemo.ui.CustomCardView
import com.ironelder.mykotlindemo.ui.FilterView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

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

    private fun getSorting(array:ArrayList<DocumentDataVo> , sortType:Int):ArrayList<DocumentDataVo>{
        array.sortBy {  obj -> obj.title }
        return ArrayList()
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
        if(!mIsBlogDataEnd && (mFilterType.contains("All") || mFilterType.contains("Blog"))){
            RetrofitForKakao.getSearchService().
                requestSearchForKakao(type = "blog", page = mBlogPage, query = mQueryString).enqueue(object : Callback<DataVo> {
                override fun onFailure(call: Call<DataVo>, t: Throwable) {
                    println("Fail5 ${t.message}")
                }

                override fun onResponse(call: Call<DataVo>, response: Response<DataVo>) {
                    if (response.isSuccessful) {
                        val blogDataVo = response.body()
                        mIsBlogDataEnd = blogDataVo?.meta?.is_end?:true
                        mItemArrayList.addAll(blogDataVo?.documents as ArrayList<DocumentDataVo>)
                        mRecyclerView.adapter?.notifyDataSetChanged()
                        mBlogPage++
                    }
                }

            })
        }

        if(!mIsCafeDataEnd && (mFilterType.contains("All") || mFilterType.contains("Cafe"))){
            RetrofitForKakao.getSearchService().
                requestSearchForKakao(type = "cafe", page = mCafePage, query = mQueryString).enqueue(object : Callback<DataVo> {
                override fun onFailure(call: Call<DataVo>, t: Throwable) {
                    println("Fail5 ${t.message}")
                }

                override fun onResponse(call: Call<DataVo>, response: Response<DataVo>) {
                    if (response.isSuccessful) {
                        val cafeDataVo = response.body()
                        mIsCafeDataEnd = cafeDataVo?.meta?.is_end?:true
                        mItemArrayList.addAll(cafeDataVo?.documents as ArrayList<DocumentDataVo>)
                        mRecyclerView.adapter?.notifyDataSetChanged()
                        mCafePage++
                    }
                }
            })
        }
    }


    private fun hideKeybaord(v: View) {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(v.applicationWindowToken, 0)
    }

    private fun filter(searchItemList:List<DocumentDataVo>, filterType:Int):ArrayList<DocumentDataVo> {
        var filteredList:ArrayList<DocumentDataVo> = ArrayList()
        for (searchItem in searchItemList) {
            if(searchItem.getType() == filterType){
                filteredList.add(searchItem)
            }
        }
        return filteredList
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

        fun setFilter(items :ArrayList<DocumentDataVo>) {
            mItemArrayList.clear()
            mItemArrayList.addAll(items)
            notifyDataSetChanged()
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            if(holder is CustomItemHolder) {
                var dataDto = mItemArrayList[position-1]
                holder.setData(dataDto)
                holder.setViewPosition(position-1)
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
        }
        inner class HeaderViewHolder:RecyclerView.ViewHolder{
            constructor(headerView:View):super(headerView)
        }
    }
}
