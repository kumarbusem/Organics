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
import com.tekkr.organics.databinding.DialogContactDetailsBinding
import com.tekkr.organics.databinding.DialogOtpBinding
import kotlinx.android.synthetic.main.dialog_contact_details.*
import kotlinx.android.synthetic.main.dialog_otp.*
import kotlinx.android.synthetic.main.dialog_otp.btnClose
import kotlinx.android.synthetic.main.dialog_otp.etPhone
import java.lang.Exception

class ContactDetailsDialog(val onSaveContactDetails: (String, String) -> Unit) : DialogFragment() {

    private lateinit var mBinding: DialogContactDetailsBinding

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
        mBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_contact_details, container, false)
        mBinding.lifecycleOwner = viewLifecycleOwner
        return mBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupViews()
    }


    private fun setupViews() {

        mBinding.apply {

            btnSave.disable()

            btnClose.setOnClickListener {
                dialog?.dismiss()
            }

            btnSave.setOnClickListener {
                onSaveContactDetails(mBinding.etName.text.toString().trim(), mBinding.etPhone.text.toString().trim())
                dialog?.dismiss()
            }

            etPhone.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) = mBinding.btnSave.enableIf(!s?.toString().isNullOrEmpty() && s?.toString()?.length == 10 && !mBinding.etName.text.isNullOrEmpty() && Patterns.PHONE.matcher(s.toString().trim()).matches())
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
            })
            etName.addTextChangedListener(object : TextWatcher {
                override fun afterTextChanged(s: Editable?) = mBinding.btnSave.enableIf(!mBinding.etPhone.text?.toString().isNullOrEmpty() && mBinding.etPhone.text?.toString()?.length == 10 && !s.isNullOrEmpty() && Patterns.PHONE.matcher(mBinding.etPhone.text?.toString()?.trim()).matches())
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) = Unit
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) = Unit
            })
        }
    }
}