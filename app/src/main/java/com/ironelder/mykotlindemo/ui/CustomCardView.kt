package com.ironelder.mykotlindemo.ui

import android.content.Context
import android.text.Html
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.ironelder.mykotlindemo.R
import com.ironelder.mykotlindemo.common.CARD_TYPE_BLOG
import com.ironelder.mykotlindemo.common.CARD_TYPE_CAFE
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
    private var mType = -1

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
        if(dataObj.cafename != null){
            mLabelImage.setImageResource(R.mipmap.cafe)
            mNameText.text = dataObj.cafename
            mType = CARD_TYPE_CAFE
        } else {
            mLabelImage.setImageResource(R.mipmap.blog)
            mNameText.text = dataObj.blogname
            mType = CARD_TYPE_BLOG
        }

        mTitleText.text = HtmlCompat.fromHtml(dataObj.title, HtmlCompat.FROM_HTML_MODE_COMPACT)
        mDateTimetext.text = dataObj.datetime
        Glide.with(mContext).load(dataObj.thumbnail).into(mThumbnailImage)
    }

    fun setPosition(position:Int){
        mPosition = position
    }

    interface CustomActionListener{
        fun onClickItem(position: Int, itemId:Int)
    }
}