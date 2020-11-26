package com.xpay.kotlin

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_pay.*


class CashPaymentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cash_payment)

        val uuid = intent.getStringExtra("UUID")
        val message = intent.getStringExtra("MESSAGE")
        txtUid.text = uuid
        txtMsg.text = message

        btnDone.setOnClickListener {
            val intent = Intent(this, ProductActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }

    }

}