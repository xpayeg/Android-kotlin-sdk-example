package com.xpay.kotlin

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.xpay.kotlinutils.XpayUtils
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_pay.*


class PayActivity : AppCompatActivity() {
    var dialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay)
        dialog = SpotsDialog.Builder().setContext(this).build()
        dialog?.show()
        webView2.loadUrl(XpayUtils.iframeUrl)
        webView2.webViewClient = object : WebViewClient() {
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {

            }

            override fun onPageFinished(view: WebView, url: String) {
                showHide(fab)
                dialog?.hide()
            }
        }
        fab.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }

    fun showHide(view: View) {
        view.visibility = if (view.visibility == View.GONE) {
            View.VISIBLE
        } else {
            View.VISIBLE
        }
    }
}