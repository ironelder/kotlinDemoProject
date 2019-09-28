package com.ironelder.mykotlindemo.ui

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import com.ironelder.mykotlindemo.R
import com.ironelder.mykotlindemo.dao.DocumentDataVo

class CustomCardView : ConstraintLayout, View.OnClickListener{
    override fun onClick(v: View?) {

    }

    private val mContext:Context
    private var mIsRead:Boolean

    private val mThumbnailImage:ImageView
    private val mLabelImage:ImageView
    private val mNameText:TextView
    private val mTitleText:TextView
    private val mDateTimetext:TextView
    private var mPosition:Int = 0

    private lateinit var mActionListener:CustomActionListener

    constructor(context: Context):super(context)
    constructor(context: Context, attrs: AttributeSet):super(context, attrs)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int):super(context, attrs, defStyleAttr)

    init{
        mContext = context
        mIsRead = false
        LayoutInflater.from(context).inflate(R.layout.cardview, this, true)
        mThumbnailImage = findViewById(R.id.thumbnail)
        mLabelImage = findViewById(R.id.label)
        mNameText = findViewById(R.id.name)
        mTitleText = findViewById(R.id.title)
        mDateTimetext = findViewById(R.id.datetime)
    }

    fun setCustomActionListener(l:CustomActionListener){
        mActionListener = l
    }

    fun setData(dataObj:DocumentDataVo){
        println(dataObj.cafename)
        mNameText.text = dataObj.blogname
    }

    fun setPosition(position:Int){
        mPosition = position
    }

    interface CustomActionListener{
        fun onClickItem(position: Int, itemId:Int)
    }
}