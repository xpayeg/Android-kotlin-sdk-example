package com.xpay.kotlin

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import com.xpay.kotlinutils.XpayUtils
import kotlinx.android.synthetic.main.activity_pay.*


class PayActivity : AppCompatActivity() {
    private var progressBar: ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        webView2.loadUrl("https://mobikul.com/blog/");
        setContentView(R.layout.activity_pay)
        webView2.loadUrl(XpayUtils.iframeUrl)
        fab.setOnClickListener {
            startActivity(Intent(this, Login::class.java))
            finish()
        }
    }
}