package com.tekkr.organics.features.payment

import android.app.Activity
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.razorpay.Checkout
import com.razorpay.PaymentData
import com.tekkr.data.models.PaymentVerifyResponse
import com.tekkr.organics.MainActivity
import com.tekkr.organics.OrganicsApplication
import com.tekkr.organics.R
import com.tekkr.organics.common.BaseAbstractFragment
import com.tekkr.organics.common.ViewModelFactory
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

            if(it.payment_verified) navigateById(R.id.action_paymentFragment_to_orderFragment)
            else{
                startPayment(it.razorpay_order_id)
            }
        })

        obsPaymentVerifyResponse.observe(viewLifecycleOwner, Observer {
            Log.e("PaymentVerifyResponse::", "$it")
            val order = mViewModel.obsOrder.value

            order?.payment_verified = it!=null && it.status.isStatusSuccess()

            repoPrefs.saveSelectedOrder(order!!)
            navigateById(R.id.action_paymentFragment_to_orderFragment)
        })
    }

    private fun startPayment(razorpayOrderId: String) {
        /*
        *  You need to pass current activity in order to let Razorpay create CheckoutActivity
        * */
        Checkout.preload(OrganicsApplication.application)
        val activity: Activity = requireActivity()
        val co = Checkout()
        co.setImage(R.drawable.app_logo_organic)

        try {
            val options = JSONObject()
            options.put("name", "TEKKR Organics")
            options.put("description", "Bill Amount")

            options.put("theme.color", "#3ab54a")
            options.put("currency", "INR")
            options.put("order_id", razorpayOrderId)
            options.put("amount", "100")
            options.put("send_sms_hash", false)

            val retryObj = JSONObject()
            retryObj.put("enabled", true)
            options.put("retry", retryObj)

            val prefill = JSONObject()
            prefill.put("email", "busem.kumar@gmail.com")
            prefill.put("contact", "9398395298")

            options.put("prefill", prefill)
            co.open(activity, options)

        }catch (e: Exception){
            mViewModel.onVerifyPayment(PaymentVerifyResponse.STATUS_FAILED, null, "Error in starting Razorpay Checkout", e.message.toString())
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(razorpayPaymentId: String?, paymentData: PaymentData?) {
        mViewModel.onVerifyPayment(PaymentVerifyResponse.STATUS_SUCCESS, paymentData, "", "")
    }

    override fun onPaymentError(errorCode: Int, errorDescription: String?, paymentData: PaymentData?) {
        mViewModel.onVerifyPayment(PaymentVerifyResponse.STATUS_FAILED, paymentData, "$errorCode", errorDescription.toString())
    }

}
