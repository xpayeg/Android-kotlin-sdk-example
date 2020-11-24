package com.xpay.kotlin

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.lifecycleScope
import com.xpay.kotlinutils.XpayUtils
import com.xpay.kotlinutils.models.api.transaction.TransactionData
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_transaction.*
import kotlinx.coroutines.launch

class TransactionActivity : AppCompatActivity() {
    var dialog: AlertDialog? = null
    var uuid: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transaction)
        dialog = SpotsDialog.Builder().setContext(this@TransactionActivity).build()


        // go to login screen when done button is pressed
        trans_btn.setOnClickListener {
            val intent = Intent(this, ProductActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }

    // Load transaction when activity launches
    override fun onStart() {
        super.onStart()
        // 01-start
        // get Transaction UUID value from the previous
        uuid = intent.getStringExtra("UUID")
        uuid?.let { loadTransaction(it) }
        // 01-end
    }

    private fun loadTransaction(uuid: String) {
        // 02-start
        lifecycleScope.launch {
            try {
                dialog?.show()
                uuid.let { it ->
                    val response = XpayUtils.getTransaction(it)
                    response?.let { updateTransactionInfo(it) }
                }
            } catch (e: Exception) {
                dialog?.hide()
                e.message?.let { it1 -> displayError(it1) }
            }

        }
        // 02-end
    }

    private fun updateTransactionInfo(tx: TransactionData) {
        // 03-start
        txt_trans_status.text = """${tx.status} TRANSACTION"""
        txt_status_egp.text = """${tx.total_amount} EGP"""
        // 03-end
        dialog?.dismiss()
    }

    private fun displayError(message: String) {
        dialog?.dismiss()
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

}