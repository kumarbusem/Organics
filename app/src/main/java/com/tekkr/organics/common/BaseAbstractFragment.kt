package com.tekkr.organics.common

import android.annotation.SuppressLint
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.Observer
import com.google.android.gms.location.*
import com.tekkr.organics.BR
import com.tekkr.organics.R
import com.tekkr.organics.features.dialogs.OTPDialog

abstract class BaseAbstractFragment<VT : BaseViewModel, BT : ViewDataBinding>
(@LayoutRes private val layoutId: Int) : BaseFragment() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    protected lateinit var mBinding: BT
    protected val mViewModel: VT by lazy { setViewModel() }
    protected val loginOTPDialog: OTPDialog by lazy {
        OTPDialog(
                onSendOTPCLicked = { phone ->
                    sendOtp(phone)
                },
                onSubmitOTPCLicked = { phone, otp ->
                    verifyOtp(phone, otp)
                })
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        mBinding = DataBindingUtil.inflate(inflater, layoutId, container, false)
        mBinding.apply {
            lifecycleOwner = viewLifecycleOwner
            setVariable(BR.viewModel, mViewModel)
        }
        mViewModel.apply {
            setupObservers().invoke(mViewModel)
            obsMessage.observe(viewLifecycleOwner, Observer {
                if (!it.isNullOrEmpty()) {
                    showToast(it)
                    obsMessage.postValue("")
                }
            })
            isUserLogout.observe(viewLifecycleOwner, Observer {
                if (it == true) {
                    navigateById(R.id.splashFragment)
                }
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


    abstract fun setViewModel(): VT
    abstract fun setupViews(): BT.() -> Unit
    abstract fun setupObservers(): VT.() -> Unit
}