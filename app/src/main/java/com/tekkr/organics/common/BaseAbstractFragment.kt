package com.tekkr.organics.common

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Intent
import android.location.Location
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.google.android.gms.auth.api.phone.SmsRetriever
import com.google.android.gms.location.*
import com.tekkr.organics.BR
import com.tekkr.organics.MainActivity
import com.tekkr.organics.R
import com.tekkr.organics.features.cart.CartFragment
import com.tekkr.organics.features.dialogs.HelpDialog
import com.tekkr.organics.features.dialogs.InfoDialog
import com.tekkr.organics.features.dialogs.OTPDialog

abstract class BaseAbstractFragment<VT : BaseViewModel, BT : ViewDataBinding>
(@LayoutRes private val layoutId: Int) : BaseFragment(), MainActivity.SMSListener {

    private lateinit var dialog: ProgressDialog
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    protected lateinit var mBinding: BT
    protected val mViewModel: VT by lazy { setViewModel() }
    protected val loginOTPDialog: OTPDialog by lazy {
        OTPDialog(
                onSendOTPCLicked = { phone ->
                    val task = SmsRetriever.getClient(requireContext()).startSmsUserConsent(null)
                    sendOtp(phone)
                },
                onSubmitOTPCLicked = { phone, otp ->
                    verifyOtp(phone, otp)
                },
                onGetPhoneNumber = {
                    getPhoneNumber()
                })
    }

    protected val helpDialog: HelpDialog by lazy {
        HelpDialog(
            onCallSupportCLicked = {
                val callIntent = Intent(Intent.ACTION_CALL)
                callIntent.data = Uri.parse("tel:9513261696")
                startActivity(callIntent)
            })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        dialog = ProgressDialog(activity)
        dialog.setMessage("Loading, Please Wait...")
        dialog.setCancelable(false)
        mBinding.apply {
            lifecycleOwner = viewLifecycleOwner
            setVariable(BR.viewModel, mViewModel)
        }
        mViewModel.apply {
            setupObservers().invoke(mViewModel)
            obsMessage.observe(viewLifecycleOwner, Observer {
                if (!it.isNullOrEmpty()) {
                    showToast(it)
                    Log.e("obsMessage::", it.toString())
                    obsMessage.postValue("")
                }
            })
            isUserLogout.observe(viewLifecycleOwner, Observer {
                if (it == true) {
                    showInfoDialogueFor("Alert", "Login session expired", "Please login", "Login", false){
                        navigateById(R.id.splashFragment)
                    }
                }
            })
            obsProgressDialog.observe(viewLifecycleOwner, Observer {
               if(it == true) dialog.show()
                else dialog.dismiss()
            })
        }

        return mBinding.root
    }

    @SuppressLint("MissingPermission")
    fun initLoation() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        var mLocationRequest = LocationRequest.create()
        mLocationRequest.interval = 60000
        mLocationRequest.fastestInterval = 50000
        mLocationRequest.priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        var mLocationCallback: LocationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult?) {
                if (locationResult == null) {
                    return
                }
                for (location in locationResult.locations) {
                    if (location != null) {

                        Log.e("LOCAATION:::", "$location")
                    }
                }
            }
        }
        LocationServices.getFusedLocationProviderClient(requireContext())
                .requestLocationUpdates(mLocationRequest, mLocationCallback, null)
    }

    @SuppressLint("MissingPermission")
    fun getLocation(res: (Location?) -> Unit) {
        LocationServices.getFusedLocationProviderClient(requireContext()).lastLocation
                .addOnSuccessListener { location: Location? ->
                    res(location)
                }.addOnFailureListener {
                    showToast("Location getting failed\nPlease check location settings")
                    res(null)
                }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mBinding.apply { setupViews().invoke(mBinding) }

        (activity as MainActivity?)?.setSMSListener(this)
    }

    private fun verifyOtp(phone: String, otp: String) {

        mViewModel.verifyOtp(phone, otp){
            loginOTPDialog.isOtpVerified.postValue(it)
        }

    }

    private fun sendOtp(phone: String) {
        mViewModel.sendOtp(phone){
            loginOTPDialog.isOtpSent.postValue(it)
        }
    }
    override fun onOTPReceived(otp: String) {
        loginOTPDialog.obsOTP.postValue(otp)
    }
    override fun onPhoneNumberReceived(phone: String) {
        loginOTPDialog.obsPhone.postValue(phone)
    }

    fun showInfoDialogueFor(title: String, message: String, subMessage: String, button: String, cancel: Boolean, onConfirmation: () -> Unit) {
        InfoDialog.Builder()
                .setTitle(title)
                .setMessage(message)
                .setSubMessage(subMessage)
                .onPrimaryAction(onConfirmation)
                .setPrimaryButtonText(button)
                .dismissOnClick()
                .setCancelable(cancel)
                .build()
                .show(childFragmentManager, CartFragment::class.java.simpleName)
    }



    abstract fun setViewModel(): VT
    abstract fun setupViews(): BT.() -> Unit
    abstract fun setupObservers(): VT.() -> Unit
}