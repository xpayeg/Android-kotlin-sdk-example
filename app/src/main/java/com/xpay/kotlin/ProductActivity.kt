package com.xpay.kotlin

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.material.chip.Chip
import com.xpay.kotlinutils.XpayUtils
import kotlinx.coroutines.launch
import com.xpay.kotlinutils.models.api.prepare.PrepareAmountData
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_login.*


class ProductActivity : AppCompatActivity() {
    var dialog: AlertDialog? = null
    var itemPrice: Double = 225.50
    var totalAmount: Double = itemPrice
    var shoesCount: Int = 1
    var color: String = "Pink"
    var size: String = "38"

    override fun onCreate(savedInstanceState: Bundle?) {

        // set XpayUtils core settings
        XpayUtils.apiKey = "3uBD5mrj.3HSCm46V7xJ5yfIkPb2gBOIUFH4Ks0Ss"
        XpayUtils.communityId = "zogDmQW"
        XpayUtils.variableAmountID = 18

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        group.isSingleSelection = true
        group1.isSingleSelection = true
        dialog = SpotsDialog.Builder().setContext(this@ProductActivity).build()

        // increase amount button handler
        fab_increase.setOnClickListener {
            shoesCount = shoesCount.plus(1)
            txt_shoes_amount.text = shoesCount.toString()
            totalAmount = shoesCount * itemPrice
        }

        // decrease amount button handler
        fab_decrease.setOnClickListener {
            if (shoesCount > 1) {
                shoesCount = shoesCount.minus(1)
                txt_shoes_amount.text = shoesCount.toString()
                totalAmount = shoesCount * itemPrice
            }
        }

        // Get the checked chip instance from sizes chip group
        group.setOnCheckedChangeListener { group, checkedId: Int ->
            val chip: Chip? = findViewById(checkedId)
            chip?.let {
                for (i in 0 until group.childCount) {
                    group.getChildAt(i).isClickable = true
                }
                it.isClickable = false
                size = it.text.toString()
            }
        }

        // Get the checked chip instance from colors chip group
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


        // Submit button handler
        btnCheckout.setOnClickListener {
            lifecycleScope.launch {
                try {
                    dialog?.show()
                    XpayUtils.prepareAmount(totalAmount)
                    goToCheckout()
                } catch (e: Exception) {
                    dialog?.hide()
                    e.message?.let { it1 -> displayError(it1) }
                }
            }
        }
    }

    // Prepare amount success case
    private fun goToCheckout() {
        // add color and size chosen as a custom fields to be saved with the transaction
        XpayUtils.addCustomField("color", color)
        XpayUtils.addCustomField("size", size)

        dialog?.dismiss()
        val intent = Intent(this, UserInfoActivity::class.java)
        startActivity(intent)
    }

    // Prepare amount failure case
    private fun displayError(res: String) {
        dialog?.dismiss()
        Toast.makeText(this, res, Toast.LENGTH_LONG).show()
    }

}