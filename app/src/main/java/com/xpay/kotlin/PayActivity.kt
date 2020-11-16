package com.xpay.kotlin

import android.app.AlertDialog
import android.content.ComponentName
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.*
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.core.content.ContextCompat
import com.xpay.kotlinutils.XpayUtils
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_pay.*


class PayActivity : AppCompatActivity() {
    var uuid: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_pay)
        if (XpayUtils.ShippingInfo != null) {
            txt_status.text = "Successful Payment"

        }
        uuid = intent.getStringExtra("UUID")
        val message = intent.getStringExtra("MESSAGE")
        txtUid.text = uuid
        txtMsg.text = message

        btnDone.setOnClickListener {
            goToTransaction(uuid!!)
        }

    }

    // go to transaction detail screen
    private fun goToTransaction(Uid: String) {
        val intent = Intent(this, TransactionActivity::class.java)
        intent.putExtra("UUID", Uid)
        startActivity(intent)
    }

}