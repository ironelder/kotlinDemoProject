package com.ironelder.mykotlindemo.component

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.text.HtmlCompat
import com.bumptech.glide.Glide
import com.ironelder.mykotlindemo.R
import com.ironelder.mykotlindemo.common.CommonUtils
import com.ironelder.mykotlindemo.common.VIEW_TYPE_DETAIL
import com.ironelder.mykotlindemo.dao.DocumentDataVo
import java.lang.Exception
import android.content.Intent
import android.app.Activity
import android.view.KeyEvent
import android.webkit.WebView
import android.webkit.WebViewClient
import kotlinx.android.synthetic.main.activity_detail.*
import android.webkit.WebSettings




class DetailActivity: AppCompatActivity() {
    private lateinit var vo:DocumentDataVo
    private lateinit var mBackButton:Button
    private lateinit var mTypeImage: ImageView
    private lateinit var mWebView:WebView
    private lateinit var mWebSettings: WebSettings
    private var mItemPosition: Int = 0
    private var mItemId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        vo = intent.extras?.get("detailData") as? DocumentDataVo?: DocumentDataVo("","","","","","","")
        mItemPosition = intent?.getIntExtra("Position",0)?:0
        mItemId = intent?.getIntExtra("ItemId",0)?:0

        setContentView(R.layout.activity_detail)
        findViewById<EditText>(R.id.search_view).visibility = View.GONE
        findViewById<Button>(R.id.search_btn).visibility = View.GONE

        mBackButton = findViewById(R.id.backBtn)
        mBackButton.visibility = View.VISIBLE
        mBackButton.setOnClickListener { view ->
            finish()
        }
        mTypeImage = findViewById(R.id.detailType)
        mTypeImage.visibility = View.VISIBLE
        mTypeImage.setImageResource(vo.getTypeImageResourceId())
        try{
            Glide.with(this@DetailActivity).load(vo.thumbnail).override(720, 720).placeholder(vo.getTypeImageResourceId()).into(findViewById(R.id.detail_thumbnail))
        }catch (e:Exception){
            findViewById<ImageView>(R.id.detail_thumbnail).setImageResource(vo.getTypeImageResourceId())
        }
        findViewById<TextView>(R.id.detail_name).text = vo.getName()
        findViewById<TextView>(R.id.detail_title).text = HtmlCompat.fromHtml(vo.title, HtmlCompat.FROM_HTML_MODE_COMPACT)
        findViewById<TextView>(R.id.detail_content).text = HtmlCompat.fromHtml(vo.contents, HtmlCompat.FROM_HTML_MODE_COMPACT)
        findViewById<TextView>(R.id.detail_datetime).text = CommonUtils.getCustomDateTime(vo.datetime, VIEW_TYPE_DETAIL)
        findViewById<TextView>(R.id.detail_url).text = vo.url

        mWebView = findViewById(R.id.webview)
        findViewById<Button>(R.id.goto_webview_btn).setOnClickListener { view ->
            if(!vo.url.isNullOrEmpty()){
                mWebView.visibility = View.VISIBLE
                webview.webViewClient = WebViewClient()
                mWebSettings = mWebView.settings
                mWebSettings.javaScriptEnabled = true
                mWebSettings.setSupportMultipleWindows(false)
                mWebSettings.javaScriptCanOpenWindowsAutomatically = false
                mWebSettings.loadWithOverviewMode = true
                mWebSettings.useWideViewPort = true
                mWebSettings.setSupportZoom(false)
                mWebSettings.builtInZoomControls = false
                mWebView.loadUrl(vo.url)
            }
        }
    }

    override fun finish() {
        if(mWebView.visibility == View.VISIBLE){
            mWebView.destroy()
            mWebView.visibility = View.GONE
        }else{
            val resultIntent = Intent()
            resultIntent.putExtra("Position", mItemPosition)
            resultIntent.putExtra("ItemId", mItemId)
            setResult(Activity.RESULT_OK, resultIntent)
            super.finish()
        }
    }
}