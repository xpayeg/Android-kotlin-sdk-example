package com.xpay.kotlin

import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.chip.Chip
import dmax.dialog.SpotsDialog
import kotlinx.android.synthetic.main.activity_product.*


class ProductActivity : AppCompatActivity() {
    var dialog: AlertDialog? = null
    var itemPrice: Double = 225.50
    var totalAmount: Double = itemPrice
    var shoesCount: Int = 1
    var color: String = "Pink"
    var size: String = "38"

    override fun onCreate(savedInstanceState: Bundle?) {
        // 01-start

        // 01-end
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_product)
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
            // 02-start
            
            // 02-end
        }
    }

    // Prepare amount success case
    private fun goToCheckout() {
        // 03-start

        // 03-end
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