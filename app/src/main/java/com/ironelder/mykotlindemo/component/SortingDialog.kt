package com.ironelder.mykotlindemo.component

import android.app.Dialog
import android.content.Context
import android.content.DialogInterface
import android.widget.Button
import android.widget.RadioGroup
import com.ironelder.mykotlindemo.R
import com.ironelder.mykotlindemo.common.SORT_TYPE_DATE_DESC
import com.ironelder.mykotlindemo.common.SORT_TYPE_TITLE_ASC

class SortingDialog:Dialog {

    private val mCloseButton: Button
    private val mConfirmButton:Button
    private var mSortGroup:RadioGroup
    private var mSortType:Int = SORT_TYPE_TITLE_ASC
    private lateinit var mDialogActionListener:DialogActionListener

    constructor(context: Context, sortType: Int):super(context){
        mSortType = sortType
        if(mSortType == SORT_TYPE_TITLE_ASC){
            mSortGroup.check(R.id.rg_asc)
        }else{
            mSortGroup.check(R.id.rg_desc)
        }
    }
    init{
        setContentView(R.layout.sorting_dialog)
        mSortGroup = findViewById(R.id.sortGroup)
        mSortGroup.setOnCheckedChangeListener { group, checkedId ->
            mSortType = if(R.id.rg_asc == checkedId){
                SORT_TYPE_TITLE_ASC
            }else{
                SORT_TYPE_DATE_DESC
            }
        }
        mConfirmButton = findViewById(R.id.confirmBtn)
        mConfirmButton.setOnClickListener {
            mDialogActionListener.setSortOrder(mSortType)
        }
        mCloseButton = findViewById(R.id.cancelBtn)
        mCloseButton.setOnClickListener {
            dismiss()
        }
    }

    fun setDialogActionListener(l:DialogActionListener){
        mDialogActionListener = l
    }

    interface DialogActionListener{
        fun setSortOrder(sortType:Int)
    }

}