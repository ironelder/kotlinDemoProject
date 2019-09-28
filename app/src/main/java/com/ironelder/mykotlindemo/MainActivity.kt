package com.ironelder.mykotlindemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ironelder.mykotlindemo.common.RetrofitForKakao
import com.ironelder.mykotlindemo.dao.DataVo
import com.ironelder.mykotlindemo.dao.DocumentDataVo
import com.ironelder.mykotlindemo.ui.CustomCardView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    private lateinit var mSearchEditText:EditText
    private lateinit var mSearchButton:Button
    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mItemArrayList:ArrayList<DocumentDataVo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mItemArrayList = ArrayList()


        mRecyclerView = findViewById(R.id.recyclerView)
        mRecyclerView.adapter = CustomRecyclerAdapter()
        mRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        mRecyclerView.setHasFixedSize(true)

        mSearchEditText = findViewById(R.id.search_view)
        mSearchButton = findViewById(R.id.search_btn)
        mSearchButton.setOnClickListener { view ->
            RetrofitForKakao.getSearchService().
                requestSearchForKakao(type = "blog", page = 1, query = mSearchEditText.text.toString()).enqueue(object : Callback<DataVo> {
                override fun onFailure(call: Call<DataVo>, t: Throwable) {
                    println("Fail5 ${t.message}")
                }

                override fun onResponse(call: Call<DataVo>, response: Response<DataVo>) {
                    if (response.isSuccessful) {
                        val dataVo = response.body()
                        mItemArrayList = dataVo?.documents as ArrayList<DocumentDataVo>
                        mRecyclerView.adapter?.notifyDataSetChanged()
                    }
                }

            })
        }
    }


    private inner class CustomRecyclerAdapter: RecyclerView.Adapter<CustomRecyclerAdapter.CustomItemHolder>(){
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomItemHolder {
            var viewHolder = CustomCardView(parent!!.context)
            viewHolder.layoutParams = RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT, RecyclerView.LayoutParams.WRAP_CONTENT)
            return CustomItemHolder(viewHolder)
        }

        override fun getItemCount(): Int {
            println(mItemArrayList?.size)
            return mItemArrayList?.size
        }

        override fun onBindViewHolder(holder: CustomItemHolder, position: Int) {
            var dataDto = mItemArrayList[position]
            holder.setData(dataDto)
            holder.setViewPosition(position)
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
    }
}
