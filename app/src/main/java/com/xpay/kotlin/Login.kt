package com.xpay.kotlin

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import com.xpay.kotlin.models.PrepareAmount
import com.xpay.kotlinutils.XpayUtils
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_login.*


class Login : AppCompatActivity() {
    var dialog: AlertDialog? = null
    var itemPrice: Double = 225.5
    var totalAmount: Double = itemPrice
    var shoesCount: Int? = 1
    var color: String = "Pink"
    var size: String = "38"
    override fun onCreate(savedInstanceState: Bundle?) {
        XpayUtils.apiKey = "3uBD5mrj.3HSCm46V7xJ5yfIkPb2gBOIUFH4Ks0Ss"
        XpayUtils.communityId = "zogDmQW"
        XpayUtils.variableAmountID = 18
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        group.isSingleSelection = true
        group1.isSingleSelection = true

        fab_increase.setOnClickListener {
            totalAmount = totalAmount.plus(this.itemPrice);
            shoesCount = shoesCount?.plus(1)
            txt_shoes_amount.text = shoesCount.toString()
        }

        fab_decrease.setOnClickListener {
            if (shoesCount!! > 1) {
                shoesCount = shoesCount?.minus(1)
                txt_shoes_amount.text = shoesCount.toString()
                totalAmount = totalAmount.minus(this.itemPrice);
            }
        }

        group.setOnCheckedChangeListener { group, checkedId: Int ->
            // Get the checked chip instance from chip group
            val chip: Chip? = findViewById(checkedId)

            chip?.let {
                for (i in 0 until group.childCount) {
                    group.getChildAt(i).isClickable = true
                }
                it.isClickable = false
                size = it.text.toString()
            }
        }

        group1.setOnCheckedChangeListener { group, checkedId: Int ->
            // Get the checked chip instance from chip group
            val chip: Chip? = findViewById(checkedId)

            chip?.let {
                for (i in 0 until group.childCount) {
                    group.getChildAt(i).isClickable = true
                }
                it.isClickable = false
                color = it.tag.toString()
            }
        }



        dialog = SpotsDialog.Builder().setContext(this@Login).build()

        btnLogin.setOnClickListener {
            dialog?.show()
            XpayUtils.addCustomField("color", color)
            XpayUtils.addCustomField("size", size)
            XpayUtils.prepareAmount(totalAmount, ::userSuccess, ::userFailure)
        }
    }

    fun userSuccess(res: PrepareAmount) {
        dialog?.dismiss()
        val amount: String = res.data.total_amount.toString()
        val intent = Intent(this, UserActivity::class.java)
        intent.putExtra("AMOUNT", amount)
        startActivity(intent)
    }

    fun userFailure(res: String) {
        dialog?.dismiss()
        Toast.makeText(this, res, Toast.LENGTH_LONG).show()
    }

}