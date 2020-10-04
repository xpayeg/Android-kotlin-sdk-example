package com.xpay.kotlin

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.xpay.kotlinutils.XpayUtils
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_pay.*


class PayActivity : AppCompatActivity() {
    var dialog: AlertDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pay)
        dialog = SpotsDialog.Builder().setContext(this).build()
        if (XpayUtils.iframeUrl != null) {
            dialog?.show()
            showHide(webView2)
            showHide(btnDone)
            constrain_status.setBackgroundColor(ContextCompat.getColor(this, R.color.dark_gray))
            webView2.loadUrl(XpayUtils.iframeUrl)
            webView2.webViewClient = object : WebViewClient() {
                override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {

                }

                override fun onPageFinished(view: WebView, url: String) {
                    showHide(fab)
                    dialog?.hide()
                }
            }
        } else {
            if (XpayUtils.shippingInfo != null && XpayUtils.payUsing == "cash") {
                txt_status.text = "Successful Payment"
                txt_status.setTextColor(Color.parseColor("#4C9A2A"));
            }
            val uuid = intent.getStringExtra("UUID")
            val message = intent.getStringExtra("MESSAGE")
            txtUid.text = uuid
            txtMsg.text = message
        }

        btnDone.setOnClickListener {
            val intent = Intent(this, Login::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
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
            View.GONE
        }
    }
}