package com.xpay.kotlin

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.xpay.kotlinutils.XpayUtils
import com.xpay.kotlinutils.models.PaymentMethods
import com.xpay.kotlinutils.models.ShippingInfo
import com.xpay.kotlinutils.models.User
import kotlinx.android.synthetic.main.activity_user.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import kotlin.collections.ArrayList


class UserActivity : AppCompatActivity() {

    var amount: Number = 0
    var adapter: ArrayAdapter<String>? = null
    var adapterCountry: ArrayAdapter<String>? = null
    var validShippingForm: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        // TODO: 2020-11-17 check if needed
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        // get the available active payment methods
        val mSpinnerData: MutableList<PaymentMethods> = XpayUtils.activePaymentMethods
        // Convert paymentMethodsList from List<PaymentMethods> to List<String>
        val paymentMethodsList: MutableList<String> = mutableListOf()
        for (i in mSpinnerData) {
            paymentMethodsList.add(i.toString())
        }
        // Populate drop down with payment methods List
        adapter = paymentMethodsList.distinct().toList().let {
            ArrayAdapter(
                this, android.R.layout.simple_spinner_item, it
            )
        }
        adapter?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinner.adapter = adapter

        // set actual amount for different payment methods
        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                when (XpayUtils.activePaymentMethods[position]) {
                    PaymentMethods.CASH -> {
                        amount = XpayUtils.PaymentOptionsTotalAmounts?.cash!!
                        XpayUtils.payUsing = PaymentMethods.CASH
                        showView(constraint_shipping)
                    }
                    PaymentMethods.CARD -> {
                        amount = XpayUtils.PaymentOptionsTotalAmounts?.card!!
                        XpayUtils.payUsing = PaymentMethods.CARD
                        hideView(constraint_shipping)
                        validShippingForm = true
                    }
                    PaymentMethods.KIOSK -> {
                        amount = XpayUtils.PaymentOptionsTotalAmounts?.kiosk!!
                        XpayUtils.payUsing = PaymentMethods.KIOSK
                        hideView(constraint_shipping)
                        validShippingForm = true
                    }
                }
                amount = String.format("%.2f", amount).toDouble()
                totalAmountTxt.text = "Total Amount: ${amount} Egp"
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // your code here
            }
        }

        val jsonFileString = getJsonDataFromAsset(applicationContext, "countries.json")
        val obj = JSONObject(jsonFileString!!)

        val countriesList = populateCountries(obj)

        // when country selected, Update its cities
        sp_country.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parentView: AdapterView<*>?,
                selectedItemView: View?,
                position: Int,
                id: Long
            ) {
                populateStates(obj, countriesList[position])
            }

            override fun onNothingSelected(parentView: AdapterView<*>?) {
                // your code here
            }
        }

        // submit button
        btnSubmit.setOnClickListener {
            // validate shipping info(in case cash collection method is selected)
            if (constraint_shipping.visibility == View.VISIBLE) {
                // get shipping info
                val street = et_street.text.toString()
                val building = et_building.text.toString()
                val apartment = et_apartment.text.toString()
                val floor = et_floor.text.toString()

                if (street.isNotEmpty() && building.isNotEmpty() && apartment.isNotEmpty() && floor.isNotEmpty()) {
                    validShippingForm = true
                    // set user billing info
                    XpayUtils.ShippingInfo = ShippingInfo(
                        "EG",
                        sp_state.selectedItem.toString(),
                        sp_country.selectedItem.toString(),
                        apartment,
                        building,
                        floor,
                        street
                    )
                } else {
                    validShippingForm = false
                    if (street.isEmpty()) {
                        et_street.error = "Enter valid street name"
                    }
                    if (building.isEmpty()) {
                        et_building.error = "Enter valid building number"
                    }
                    if (apartment.isEmpty()) {
                        et_apartment.error = "Enter valid apartment number"
                    }
                    if (floor.isEmpty()) {
                        et_floor.error = "Enter valid floor number"
                    }
                }
            }

            // validate billing info
            val fullName: String = userName.text.toString()
            val email: String = userEmail.text.toString()
            val phone = "+2${userPhone.text}"

            if (fullName.isNotEmpty() && email.isEmailValid() && phone.length >= 9 && validShippingForm) {
                // set user billing info
                XpayUtils.userInfo = User(fullName, email, phone)
                val intent = Intent(this, MainActivity::class.java)
                intent.putExtra("TOTAL_AMOUNT", amount.toString())
                startActivity(intent)
            } else {
                if (fullName.isEmpty())
                    userName.error = "Enter valid Full Name"
                if (!email.isEmailValid())
                    userEmail.error = "Enter valid Email"
                if (phone.isEmpty() || phone.length < 9)
                    userPhone.error = "Enter valid Phone Number"
            }
        }
    }

    private fun String.isEmailValid(): Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this)
            .matches()
    }

    private fun getJsonDataFromAsset(context: Context, fileName: String): String? {
        val jsonString: String
        try {
            jsonString = context.assets.open(fileName).bufferedReader().use { it.readText() }
        } catch (ioException: IOException) {
            ioException.printStackTrace()
            return null
        }
        return jsonString
    }

    // populate countries dropdown list
    fun populateCountries(obj: JSONObject): ArrayList<String> {
        val countriesList = ArrayList<String>()
        for (i in obj.keys()) {
            countriesList.add(i)
        }
        adapterCountry = ArrayAdapter(
            this, android.R.layout.simple_spinner_item, countriesList
        )
        adapterCountry?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sp_country.adapter = adapterCountry
        return countriesList
    }

    // populate cities dropdown list
    fun populateStates(obj: JSONObject, key: String) {
        val citiesArray: JSONArray = obj.optJSONArray(key)!!
        val citiesList = ArrayList<String>()
        for (i in 0 until citiesArray.length()) {
            citiesList.add(citiesArray.getString(i))
        }
        adapterCountry = ArrayAdapter(
            this, android.R.layout.simple_spinner_item, citiesList
        )
        adapterCountry?.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        sp_state.adapter = adapterCountry
    }

    // Show shipping info form
    fun showView(view: View) {
        view.visibility = View.VISIBLE
    }

    // Hide shipping info form
    fun hideView(view: View) {
        view.visibility = View.GONE
    }
}