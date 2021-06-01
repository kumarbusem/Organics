package com.tekkr.organics.features.payment

import android.app.Activity
import android.util.Log
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.razorpay.Checkout
import com.tekkr.data.roomDatabase.CartItem
import com.tekkr.organics.MainActivity
import com.tekkr.organics.OrganicsApplication
import com.tekkr.organics.R
import com.tekkr.organics.common.BaseAbstractFragment
import com.tekkr.organics.common.ViewModelFactory
import com.tekkr.organics.common.hide
import com.tekkr.organics.common.show
import com.tekkr.organics.databinding.FragmentCartBinding
import com.tekkr.organics.databinding.FragmentPaymentBinding
import com.tekkr.organics.databinding.FragmentProfileBinding
import com.tekkr.organics.features.cart.CartFragment
import com.tekkr.organics.features.dialogs.InfoDialog
import com.tekkr.organics.features.dialogs.OTPDialog
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

        mViewModel.placeOrder()

    }

    override fun setupObservers(): PaymentViewModel.() -> Unit = {

    }

    private fun startPayment() {
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
            options.put("amount", "100")
            options.put("send_sms_hash", false)

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
        mBinding.tvStatus.text = razorpayPaymentId
    }

    override fun onPaymentError(errorCode: Int, response: String?) {
        mBinding.tvStatus.text = response
    }

    fun showInfoDialogueFor(title: String, message: String, subMessage: String, onConfirmation: () -> Unit) {
        InfoDialog.Builder()
                .setTitle(title)
                .setMessage(message)
                .setSubMessage(subMessage)
                .onPrimaryAction(onConfirmation)
                .dismissOnClick()
                .build()
                .show(childFragmentManager, CartFragment::class.java.simpleName)
    }

}
