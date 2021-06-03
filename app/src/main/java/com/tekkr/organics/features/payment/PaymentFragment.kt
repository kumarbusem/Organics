package com.tekkr.organics.features.payment

import android.app.Activity
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.tekkr.data.models.Error
import com.tekkr.data.models.RazorPayResponse
import com.tekkr.data.models.SimpleResponse
import com.tekkr.organics.MainActivity
import com.tekkr.organics.OrganicsApplication
import com.tekkr.organics.R
import com.tekkr.organics.common.BaseAbstractFragment
import com.tekkr.organics.common.ViewModelFactory
import com.tekkr.organics.common.isAlreadyPaid
import com.tekkr.organics.common.isStatusSuccess
import com.tekkr.organics.databinding.FragmentPaymentBinding
import org.json.JSONObject

class PaymentFragment : BaseAbstractFragment<PaymentViewModel, FragmentPaymentBinding>(R.layout.fragment_payment), MainActivity.PaymentListener {

    override fun setViewModel(): PaymentViewModel =
            ViewModelProvider(this@PaymentFragment, ViewModelFactory {
                PaymentViewModel(requireActivity().application)
            }).get(PaymentViewModel::class.java)

    override fun setupViews(): FragmentPaymentBinding.() -> Unit = {
        (activity as MainActivity?)?.setPaymentsListener(this@PaymentFragment)

        btnBack.setOnClickListener {
            navigateBack()
        }

    }

    override fun setupObservers(): PaymentViewModel.() -> Unit = {

        obsOrder.observe(viewLifecycleOwner, Observer {
            startPayment()
        })
        obsPaymentVerify.observe(viewLifecycleOwner, Observer {
            Log.e("VERIFY::", "$it")
            if (it.status.isStatusSuccess()) {
                val order = mViewModel.obsOrder.value
                order?.payment_verified = true
                repoPrefs.saveSelectedOrder(order!!)
                navigateBack()
            } else {
                showInfoDialogueFor("Alert", "Payment verification failed", "${it.message}\nPlease try again", "TRY AGAIN", false) {
                    mViewModel.verifyPayment()
                }
            }
        })
        obsRazorPayStatus.observe(viewLifecycleOwner, { razorPayStatus ->
            Log.e("PAYMENT::", razorPayStatus.toString())
            when {
                razorPayStatus.status.isStatusSuccess() -> {
                    verifyPayment()
                }
                razorPayStatus.status.isAlreadyPaid() -> {
                    showInfoDialogueFor("Payment Issue", "Your payment is not verified for this order", "Please contact our customer care", "CALL", false) {
                        navigateBack()
                    }
                }
                else -> {
                    showInfoDialogueFor("Payment Failed", razorPayStatus.error.description, razorPayStatus.error.reason, "TRY AGAIN", false) {
                        startPayment()
                    }
                }
            }
        })
    }

    private fun startPayment() {

        Checkout.preload(OrganicsApplication.application)
        val activity: Activity = requireActivity()
        val co = Checkout()
        co.setImage(R.drawable.app_logo_organic)
        try {
            val options = JSONObject()
            options.put("name", "TEKKR Organics")
            options.put("description", "Bill Amount")

            options.put("theme.color", "#3ab54a")
            options.put("order_id", mViewModel.obsOrder.value?.razorpay_order_id)
            options.put("send_sms_hash", false)

            val retryObj = JSONObject()
            retryObj.put("enabled", true)
            options.put("retry", retryObj)

            val prefill = JSONObject()
            prefill.put("contact", repoPrefs.getLoggedInUser()?.phone_number)
            options.put("prefill", prefill)
            co.open(activity, options)

        } catch (e: Exception) {
            showInfoDialogueFor("Alert", "Payment initialization failed, please try again", e.message.toString(), "TRY AGAIN", false) {
                startPayment()
            }
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(razorpayPaymentId: String?, paymentData: PaymentData?) {
        mViewModel.obsRazorPayStatus.postValue(RazorPayResponse(RazorPayResponse.STATUS_SUCCESS, paymentData, Error()))
    }

    override fun onPaymentError(errorCode: Int, errorDescription: String?, paymentData: PaymentData?) {
        val error = Gson().fromJson<RazorPayResponse>(errorDescription, object : TypeToken<RazorPayResponse>() {}.type)
        if (errorDescription != null && errorDescription.contains("Payment already done"))
            mViewModel.obsRazorPayStatus.postValue(RazorPayResponse(RazorPayResponse.STATUS_ALREADY_PAID, paymentData, Error()))
        else
            mViewModel.obsRazorPayStatus.postValue(RazorPayResponse(RazorPayResponse.STATUS_FAILED, paymentData, error.error))
    }

}
