package com.tekkr.organics.features.payment

import android.app.Activity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.razorpay.Checkout
import com.tekkr.data.models.Order
import com.tekkr.organics.MainActivity
import com.tekkr.organics.OrganicsApplication
import com.tekkr.organics.R
import com.tekkr.organics.common.BaseAbstractFragment
import com.tekkr.organics.common.ViewModelFactory
import com.tekkr.organics.common.hide
import com.tekkr.organics.common.show
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

        tvPlaceOrder.setOnClickListener {
            mViewModel.placeOrder()
        }

        mViewModel.placeOrder()

    }

    override fun setupObservers(): PaymentViewModel.() -> Unit = {

        mViewModel.obsPlaceOrderResponse.observe(viewLifecycleOwner, Observer { response ->
            if (response.status == Order.STATUS_CREATED) {
                startPayment(response.razorpay_order_id)
                mViewModel.clearCartItems()
            }
            mBinding.mcvPlaceOrder.show()
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
            mBinding.tvStatus.text = e.message.toString()
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(razorpayPaymentId: String?) {
        mBinding.tvPayment.text = "Completed"
        mBinding.btnPayment.hide()
    }

    override fun onPaymentError(errorCode: Int, response: String?) {

    }


}
