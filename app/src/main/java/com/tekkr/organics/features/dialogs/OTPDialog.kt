package com.tekkr.organics.features.dialogs


import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.os.CountDownTimer
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.tekkr.data.models.SimpleResponse
import com.tekkr.data.roomDatabase.BigItem
import com.tekkr.organics.R
import com.tekkr.organics.common.*
import com.tekkr.organics.databinding.DialogOtpBinding
import kotlinx.android.synthetic.main.dialog_otp.*
import java.lang.Exception


class OTPDialog(val onSendOTPCLicked: (String) -> Unit, val onSubmitOTPCLicked: (String, String) -> Unit) : DialogFragment() {

    private lateinit var mBinding: DialogOtpBinding

    var cTimer: CountDownTimer? = null
    var isOtpSent: MutableLiveData<SimpleResponse> = MutableLiveData()
    var isOtpVerified: MutableLiveData<SimpleResponse> = MutableLiveData()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return super.onCreateDialog(savedInstanceState).apply {
            setCanceledOnTouchOutside(true)
            window?.apply {
                attributes?.windowAnimations = R.style.DialogSideInOutAnimation
                setBackgroundDrawableResource(android.R.color.transparent)
                setGravity(Gravity.CENTER_VERTICAL)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.apply {
            setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        }
        dialog?.setCancelable(false)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_otp, container, false)
        mBinding.lifecycleOwner = viewLifecycleOwner
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }

    private fun setUpTimer() {
        tvResend.hide()
        cTimer = object : CountDownTimer(20000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                try {
                    tvTimer.setText("You can resend OTP in " + millisUntilFinished / 1000 + " sec")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }

            override fun onFinish() {
                try {
                    tvTimer.setText("Click here to")
                    tvResend.show()
                    cTimer?.cancel()
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }.start()
    }

    //cancel timer
    fun cancelTimer() {
        if (cTimer != null) {
            Log.e("TIMER", "CANCELLED")
            cTimer!!.cancel()
        }
    }


    override fun onDismiss(dialog: DialogInterface) {
        mBinding.etOTP.setText("")
        cancelTimer()
        mBinding.btnSubmitOTP.disable()
        mBinding.btnSubmitPhone.disable()
        mBinding.cvVerifyOtp.hide()
        isOtpVerified.postValue(SimpleResponse("failed", ""))
        isOtpSent.postValue(SimpleResponse("failed", ""))
        mBinding.tvError.hide()
        super.onDismiss(dialog)
    }

    private fun setupViews() {

        mBinding.apply {

            btnSubmitOTP.disable()
            btnSubmitPhone.disable()
            cvVerifyOtp.hide()

            btnClose.setOnClickListener {
                dialog?.dismiss()
            }

            tvResend.setOnClickListener {
                onSendOTPCLicked(mBinding.etPhone.text.toString().trim())
                setUpTimer()
            }

            isOtpSent.observe(viewLifecycleOwner, Observer {
                Log.e("IS OTP SENT::", "$it")
                mBinding.loginProgress.hide()
                mBinding.btnSubmitPhone.enable()
                if (it != null && it.status.isStatusSuccess()) {
                    mBinding.tvPhone.text = "OTP sent to ${mBinding.etPhone.text.toString().trim()}"
                    mBinding.cvSendOtp.hide()
                    mBinding.cvVerifyOtp.show()
                    setUpTimer()
                } else {
                    mBinding.tvError.text = it.message.trim()
                    mBinding.tvError.show()
                }
            })
            isOtpVerified.observe(viewLifecycleOwner, Observer {
                Log.e("IS OTP VERIFIED::", "$it")
                mBinding.loginProgress.hide()
                mBinding.btnSubmitOTP.enable()
                if (it != null && it.status.isStatusSuccess()) {
                    dialog?.dismiss()
                } else {
                    mBinding.tvError.text = it.message.trim()
                    mBinding.tvError.show()
                }
            })

            btnSubmitPhone.setOnClickListener {
                onSendOTPCLicked(mBinding.etPhone.text.toString().trim())
                mBinding.btnSubmitPhone.disable()
                mBinding.tvError.hide()
                mBinding.loginProgress.show()
            }

            btnSubmitOTP.setOnClickListener {
                onSubmitOTPCLicked(mBinding.etPhone.text.toString().trim(), mBinding.etOTP.text.toString().trim())
                mBinding.btnSubmitOTP.disable()
                mBinding.tvError.hide()
                mBinding.loginProgress.show()
            }



            etOTP.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) = mBinding.btnSubmitOTP.enableIf(!s?.toString().isNullOrEmpty() && s?.toString()?.length == 4)
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
            })

            etPhone.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) = mBinding.btnSubmitPhone.enableIf(!s?.toString().isNullOrEmpty() && s?.toString()?.length == 10  && Patterns.PHONE.matcher(s.toString().trim()).matches())
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
            })
        }
    }
}