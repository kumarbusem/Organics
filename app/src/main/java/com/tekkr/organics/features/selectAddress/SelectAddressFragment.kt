package com.tekkr.organics.features.selectAddress


import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.common.api.Status
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.model.Place
import com.google.android.libraries.places.widget.AutocompleteSupportFragment
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener
import com.tekkr.data.roomDatabase.Address
import com.tekkr.organics.R
import com.tekkr.organics.common.BaseAbstractFragment
import com.tekkr.organics.common.ViewModelFactory
import com.tekkr.organics.databinding.FragmentSelectAddressBinding
import java.util.*


class SelectAddressFragment :  BaseAbstractFragment<SelectAddressViewModel, FragmentSelectAddressBinding>(R.layout.fragment_select_address),
        RecentAddressListAdapter.ItemSelectionCallback {
    private val mAdapter: RecentAddressListAdapter by lazy {
        RecentAddressListAdapter(this@SelectAddressFragment)
    }
    override fun setViewModel(): SelectAddressViewModel =
            ViewModelProvider(this@SelectAddressFragment, ViewModelFactory {
                SelectAddressViewModel(requireActivity().application)
            }).get(SelectAddressViewModel::class.java)

    override fun setupViews(): FragmentSelectAddressBinding.() -> Unit = {

        rvFavAddressList.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mAdapter
        }

        ivBack.setOnClickListener { navigateBack() }
        initPlaceAutoComplete()

    }

    private fun initPlaceAutoComplete() {
        if (!Places.isInitialized()) {
            Places.initialize(requireContext(), getString(R.string.api_key), Locale.US);
        }
        // Initialize the AutocompleteSupportFragment.
        var autocompleteFragment = childFragmentManager.findFragmentById(R.id.autocomplete_fragment) as AutocompleteSupportFragment

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(listOf(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG))

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(object : PlaceSelectionListener {
            override fun onPlaceSelected(place: Place) {

                mViewModel.setSavedAddress(place)
                navigateBack()
            }

            override fun onError(status: Status) {
                Log.e("PLACE:::::", "An error occurred: $status")
            }
        })
    }
    override fun setupObservers(): SelectAddressViewModel.() -> Unit = {
       obsSavedAddressList.observe(viewLifecycleOwner, androidx.lifecycle.Observer {list ->
           if (list.isNullOrEmpty()) {
               mAdapter.submitList(emptyList())
           } else {
               mAdapter.submitList(list.toMutableList())
           }
       })
    }
    override fun onAddressSelected(recentAddress: Address) {
        navigateBack()
    }
}


