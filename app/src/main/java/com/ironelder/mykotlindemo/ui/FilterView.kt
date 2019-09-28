package com.ironelder.mykotlindemo.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.Spinner
import androidx.constraintlayout.widget.ConstraintLayout
import com.ironelder.mykotlindemo.R
import android.widget.ArrayAdapter



class FilterView:ConstraintLayout {

    private var mFilterTypeText:Spinner
    private var mSortFilterBtn:Button
    private lateinit var mFilterActionListener:FilterActionListener


    private val mSpinnerItems = arrayOf("All", "Blog", "Cafe")

    constructor(context: Context):super(context)
    constructor(context: Context, attrs: AttributeSet):super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int):super(context, attrs, defStyleAttr)

    init {
        LayoutInflater.from(context).inflate(R.layout.filterview, this, true)
        mFilterTypeText = findViewById(R.id.filter_type)
        mFilterTypeText.adapter = ArrayAdapter(context, android.R.layout.simple_spinner_dropdown_item, mSpinnerItems)
        mFilterTypeText.onItemSelectedListener = object:AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }

            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                mFilterActionListener.onSelectedItem(mSpinnerItems[position])            }

        }

        mSortFilterBtn = findViewById(R.id.sort_filter_btn)
        mSortFilterBtn.setOnClickListener { view ->
            mFilterActionListener.onSortButtonClick()
        }
    }

    fun setFilterActionListener(l:FilterActionListener){
        mFilterActionListener = l
    }

    interface FilterActionListener{
        fun onSelectedItem(selectItem:String)
        fun onSortButtonClick()
    }
}