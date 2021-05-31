package com.tekkr.organics.features.newAddress


import android.Manifest
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MapStyleOptions
import com.tekkr.organics.R
import com.tekkr.organics.common.BaseAbstractFragment
import com.tekkr.organics.common.ViewModelFactory
import com.tekkr.organics.databinding.FragmentNewAddressBinding
import kotlinx.coroutines.launch
import java.util.*
import java.util.regex.Matcher
import java.util.regex.Pattern


class NewAddressFragment : BaseAbstractFragment<NewAddressViewModel, FragmentNewAddressBinding>(R.layout.fragment_new_address), OnMapReadyCallback, GoogleMap.OnMapLoadedCallback {
    lateinit var googleMap: GoogleMap
    override fun setViewModel(): NewAddressViewModel =
            ViewModelProvider(this@NewAddressFragment, ViewModelFactory {
                NewAddressViewModel(requireActivity().application)
            }).get(NewAddressViewModel::class.java)

    override fun setupViews(): FragmentNewAddressBinding.() -> Unit = {
        ivBack.setOnClickListener { navigateBack() }
        setUpMap()
        tvDeliverHere.setOnClickListener { setDeliveryAddress() }
    }

    private fun setUpMap() {
        var mMapFragment = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mMapFragment?.getMapAsync(this)
    }

    override fun onMapReady(gMap: GoogleMap?) {
        if (gMap != null) {
            googleMap = gMap
            googleMap.setOnMapLoadedCallback(this)
            googleMap.clear()
            doBasicThings()
            showMylocationOnCamera()
        }
    }

    private fun addCameraListener() {
        googleMap.setOnCameraIdleListener {
            getAddressFromLocation(googleMap.cameraPosition.target)
        }
    }

    private fun getAddressFromLocation(latLng: LatLng) {
        ioScope.launch {
            val geocoder = Geocoder(requireContext(), Locale.ENGLISH)
            try {
                val addresses: MutableList<Address>? = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1)
                if (!addresses.isNullOrEmpty()) {
                    Log.e("Address From Location::", addresses.toString())
                    val address = addresses[0].getAddressLine(0).toString()
                    repoPrefs.saveTempAddress(
                            com.tekkr.data.roomDatabase.Address(
                                    name = address.substring(0, address.indexOf(",")),
                                    line2 = address,
                                    latitude = latLng.latitude,
                                    longitude = latLng.longitude
                            )
                    )
                    mViewModel.getAddress()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("ERROR::", e.printStackTrace().toString())
            }
        }

    }

    private fun doBasicThings() {
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return
        }
        googleMap.isMyLocationEnabled = true
        googleMap.uiSettings.isCompassEnabled = true
        googleMap.uiSettings.isMyLocationButtonEnabled = true
        googleMap.uiSettings.isZoomControlsEnabled = false
        googleMap.setMapStyle(MapStyleOptions.loadRawResourceStyle(requireContext(), R.raw.style_json))

    }

    private fun showMylocationOnCamera() {

        val address = repoPrefs.getTempAddress()
        if (address == null || address.line2.isNullOrEmpty()) {
            com.tekkr.organics.common.getLocation(requireContext()) {
                if (it != null)
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(it.latitude, it.longitude), 16f))
                else
                    googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(17.3850, 78.4867), 16.toFloat()))
            }
            addCameraListener()
        } else {
            Log.e("ADDRESSL:::", "${address.latitude}, ${address.longitude}")
            googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(LatLng(address.latitude, address.longitude), 16.toFloat()))
        }

    }

    override fun setupObservers(): NewAddressViewModel.() -> Unit = {

    }


    private fun setDeliveryAddress() {

        when {
            mBinding.etAddress.text.isNullOrEmpty() -> showToast("Please enter address")
            mBinding.etPin.text.isNullOrEmpty() -> showToast("Please enter pincode")
            else -> {
                val tempAddress = repoPrefs.getTempAddress()
                Log.e("TEMP ADDRESS::", tempAddress.toString())
                if(tempAddress != null){
                    if(!mBinding.etPlaceName.text.isNullOrEmpty())  tempAddress?.name = mBinding.etPlaceName.text.toString()
                    tempAddress.line1 = mBinding.etAddress.text.toString().trim()
                    tempAddress.pincode = mBinding.etPin.text.toString().trim()
                    repoPrefs.saveAddress(tempAddress)
                    mViewModel.setSavedAddress(tempAddress)
                    repoPrefs.clearTempAddress()
                    navigateById(R.id.action_newAddressFragment_to_cartFragment)
                } else{
                    val address = com.tekkr.data.roomDatabase.Address(name = mBinding.etPlaceName.text.toString(), line1 = mBinding.etAddress.text.toString().trim(), pincode = mBinding.etPin.text.toString().trim())
                    repoPrefs.saveAddress(address)
                    mViewModel.setSavedAddress(address)
                    repoPrefs.clearTempAddress()
                    navigateById(R.id.action_newAddressFragment_to_cartFragment)
                }
            }
        }
    }

    fun String.extractDigits(): String? {
        val p: Pattern = Pattern.compile("(\\d{6})")
        val m: Matcher = p.matcher(this)
        return if (m.find()) {
            m.group(0)
        } else ""
    }

    override fun onMapLoaded() {
        addCameraListener()
    }


}


