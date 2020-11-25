package com.xpay.kotlin

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import com.xpay.kotlinutils.XpayUtils
import com.xpay.kotlinutils.models.api.pay.PayData
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.lang.Exception

class PaymentPreviewActivity : AppCompatActivity() {
    var dialog: AlertDialog? = null
    private var isCardPayment = false
    var uuid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        dialog = SpotsDialog.Builder().setContext(this@PaymentPreviewActivity).build()

        // 01-start

        // 01-end

        // Confirm button method
        Confirmbtn.setOnClickListener {
            // 02-start

            // 02-end
        }

    }

    // when Payment successful
    private fun completePayment(response: PayData) {
        dialog?.dismiss()
        // 03-start

        // 03-end
    }

    // method to handle web view dismiss case
    // when the user dismisses the web view then navigate to transaction activity which shows him the transaction info
    override fun onRestart() {
        super.onRestart()
        // 04-start

        // 04-end
    }
}
