package com.xpay.kotlin

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.xpay.kotlin.models.*
import kotlinx.android.synthetic.main.activity_movie.*



class MovieActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_movie)
//        Utils.apiKey = "3uBD5mrj.3HSCm46V7xJ5yfIkPb2gBOIUFH4Ks0Ss"
//        Utils.amount = 6820
//        Utils.communityId = "zogDmQW"
//        Utils.currency = "EGP"
//        Utils.payUsing = "card"
//        Utils.variableAmountID = 18
//
//        Utils.user= User("Mahmoud Aziz","mabdelaziz@xpay.app","+201226476026")

        fab.setOnClickListener {
//            progress_bar.visibility = View.VISIBLE
//            prepareAmount(1296,"zogDmQW",::userSuccess,::userFailure)
//            payBill(7000, Utils.apiKey!!, ::userSuccess, ::userFailure)
//            getTransaction("3uBD5mrj.3HSCm46V7xJ5yfIkPb2gBOIUFH4Ks0Ss","zogDmQW","f8d0d7f4-16e6-4290-987f-cdf5b759baa6",::transactionSuccess,::userFailure)
        }
    }
//
//    fun handleOnSuccess(res: String): String {
//        val data = JSONObject(res).get("results")
//        progress_bar.visibility = View.GONE
//        recyclerView.apply {
//            setHasFixedSize(true)
//            layoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
//            val gson = GsonBuilder().create()
//            val objectList =
//                gson.fromJson(data.toString(), Array<Result>::class.java).asList()
//            adapter = MoviesAdapter(objectList)
//        }
//        return res
//    }

    fun userSuccess(res: PayResponse) {
        progress_bar.visibility = View.GONE
        Toast.makeText(this, res.data.iframe_url, Toast.LENGTH_LONG).show()
    }

    fun userFailure(res: String) {
        progress_bar.visibility = View.GONE
        Toast.makeText(this, res, Toast.LENGTH_LONG).show()
    }

    fun handleOnFailure(th: String): String {
        progress_bar.visibility = View.GONE
        Toast.makeText(this@MovieActivity, th, Toast.LENGTH_LONG).show()
        return th
    }

//    fun getTransaction(
//        token: String,
//        communityID: String,
//        transactionUid: String,
//        onSuccess: (Transaction) -> Unit,
//        onFail: (String) -> Unit
//    ) {
//        val request = XpayBuilder.buildService(TestEndpoints::class.java)
//        val call = request.getTransaction(token, communityID, transactionUid)
//        call.enqueue(object : Callback<Transaction> {
//            override fun onResponse(call: Call<Transaction>, response: Response<Transaction>) {
//                if (response.body() != null && response.isSuccessful) {
//                    onSuccess(response.body()!!)
//                }
//            }
//
//            override fun onFailure(call: Call<Transaction>, t: Throwable) {
//                onFail(t.message.toString())
//            }
//        })
//    }
//
//    fun payBill(
//        maount: Number,
//        token: String,
//        onSuccess: (PayResponse) -> Unit,
//        onFail: (String) -> Unit
//    ) {
//        val user:User= Utils.user!!
//        val billingData: HashMap<String, Any> = HashMap<String, Any>()
//        val requestBody: HashMap<String, Any> = HashMap<String, Any>()
//
//        billingData.put("name", user.name)
//        billingData.put("email", user.email)
//        billingData.put("phone_number", user.phone)
//        Utils.amount?.let { requestBody.put("amount", it) }
//        Utils.currency?.let { requestBody.put("currency", it) }
//        Utils.variableAmountID?.let { requestBody.put("variable_amount_id", it) }
//        Utils.communityId?.let { requestBody.put("community_id", it) }
//        Utils.payUsing?.let { requestBody.put("pay_using", it) }
//        requestBody.put("billing_data", billingData)
//
//        val request = XpayBuilder.buildService(TestEndpoints::class.java)
//        val call = request.payBill(token, requestBody)
//        call.enqueue(object : Callback<PayResponse> {
//            override fun onResponse(call: Call<PayResponse>, response: Response<PayResponse>) {
//                if (response.body() != null && response.isSuccessful) {
//                    onSuccess(response.body()!!)
//                }
//            }
//
//            override fun onFailure(call: Call<PayResponse>, t: Throwable) {
//                onFail(t.message.toString())
//            }
//        })
//    }

//    fun prepareAmount(
//        amount: Number,
//        communityID: String,
//        onSuccess: (PrepareAmount) -> Unit,
//        onFail: (String) -> Unit
//    ) {
//        val hashMap: HashMap<String, Any> = HashMap<String, Any>()
//        hashMap.put("amount", amount)
//        hashMap.put("community_id", communityID)
//        val request = XpayBuilder.buildService(TestEndpoints::class.java)
//        val call = request.prepareAmount(hashMap, "3uBD5mrj.3HSCm46V7xJ5yfIkPb2gBOIUFH4Ks0Ss")
//        call.enqueue(object : Callback<PrepareAmount> {
//            override fun onResponse(call: Call<PrepareAmount>, response: Response<PrepareAmount>) {
//                if (response.body() != null && response.isSuccessful) {
//                    onSuccess(response.body()!!)
//                }
//            }
//
//            override fun onFailure(call: Call<PrepareAmount>, t: Throwable) {
//                onFail(t.message.toString())
//            }
//        })
//    }
//
//    fun getUserMovie(
//        id: String,
//        token: String,
//        onSuccess: (String) -> Unit,
//        onFail: (String) -> Unit
//    ) {
//        progress_bar.visibility = View.VISIBLE
//        val request = XpayBuilder.buildService(TestEndpoints::class.java)
//        val call = request.loadCards(token, id)
//        call.enqueue(object : Callback<ResponseBody> {
//            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
//                if (response.body() != null && response.isSuccessful) {
//                    println(response.body()?.string())
//                }
//                progress_bar.visibility = View.GONE
//                response.body()?.string()?.let { onSuccess(it) }
//            }
//
//            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
//                println("Fail")
//                progress_bar.visibility = View.GONE
//                onFail(t.message.toString())
//            }
//        })
//    }

//    fun transactionSuccess(transaction: Transaction) {
//        progress_bar.visibility = View.GONE
//        Toast.makeText(this, transaction.data.uuid, Toast.LENGTH_LONG).show()
//    }
}


