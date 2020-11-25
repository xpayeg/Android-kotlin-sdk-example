package com.xpay.kotlin

import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
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
        uuid = intent.getStringExtra("UUID")

        // go to login screen
        trans_btn.setOnClickListener {
            val intent = Intent(this, ProductActivity::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
        }
    }

    private fun loadTransaction(Uid: String) {
        // 02-start

        // 02-end
    }

    // Load transaction when activity launched
    override fun onStart() {
        super.onStart()
        // 01-start

        // 01-end
    }

    private fun updateTransaction(res: TransactionData) {
        txt_trans_status.text = """${res.status} TRANSACTION"""
        txt_trans_uid.text = res.uuid
        txt_status_egp.text = """${res.total_amount} EGP"""

        when (res.status) {
            "SUCCESSFUL" -> {
                status_imageView.setImageResource(R.drawable.ic_transaction_success);
                txt_status_egp.setTextColor(Color.parseColor("#36DC68"))
                txt_trans_status.setTextColor(Color.parseColor("#36DC68"))
                status_imageView.background.setTint(
                    ContextCompat.getColor(
                        this,
                        R.color.trans_successs
                    )
                )
            }
            "FAILED" -> {
                status_imageView.setImageResource(R.drawable.ic_transaction_failed);
                txt_status_egp.setTextColor(Color.parseColor("#DB0012"))
                txt_trans_status.setTextColor(Color.parseColor("#DB0012"))
                status_imageView.background.setTint(
                    ContextCompat.getColor(
                        this,
                        R.color.trans_failed
                    )
                )

            }
            "PENDING" -> {
                status_imageView.setImageResource(R.drawable.ic_transaction_pending)
                txt_status_egp.setTextColor(Color.parseColor("#B7B7B7"))
                txt_trans_status.setTextColor(Color.parseColor("#B7B7B7"))
                status_imageView.background.setTint(
                    ContextCompat.getColor(
                        this,
                        R.color.trans_pending
                    )
                )

            }
        }
        dialog?.dismiss()
    }

    private fun displayError(res: String) {
        dialog?.dismiss()
        Toast.makeText(this, res, Toast.LENGTH_LONG).show()
    }

}