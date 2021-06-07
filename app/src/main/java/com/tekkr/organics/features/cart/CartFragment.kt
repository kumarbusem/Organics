package com.tekkr.organics.features.cart

import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.tekkr.data.models.Customer
import com.tekkr.data.models.Order
import com.tekkr.data.roomDatabase.CartItem
import com.tekkr.organics.BuildConfig
import com.tekkr.organics.R
import com.tekkr.organics.common.*
import com.tekkr.organics.common.PermissionManager.Companion.PERMISSION_REQUEST_CODE
import com.tekkr.organics.databinding.FragmentCartBinding
import com.tekkr.organics.features.dialogs.ContactDetailsDialog


class CartFragment : BaseAbstractFragment<CartViewModel, FragmentCartBinding>(R.layout.fragment_cart), CartListAdapter.ItemCallback{


    private val mPermissionManager: PermissionManager by lazy { PermissionManager(this@CartFragment) }
    private var snackbarShow: Boolean = false
    private val mCartAdapter: CartListAdapter by lazy {
        CartListAdapter(this@CartFragment, CartListAdapter.TYPE_CART)
    }

    private val contactDetailsDialog: ContactDetailsDialog by lazy {
        ContactDetailsDialog(onSaveContactDetails = { name, phone ->
            repoPrefs.saveContactDetails(Customer(name, phone))
            mViewModel.getContactDetails()
        })
    }

    override fun setViewModel(): CartViewModel =
            ViewModelProvider(this@CartFragment, ViewModelFactory {
                CartViewModel(requireActivity().application)
            }).get(CartViewModel::class.java)

    override fun setupViews(): FragmentCartBinding.() -> Unit = {

        rvCartItems.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mCartAdapter
        }

        swipeRefreshLayout.setOnRefreshListener {
            mViewModel.getItems()
        }

        btnBack.setOnClickListener {
            navigateBack()
        }
        cvLogin.setOnClickListener {
            loginOTPDialog.show(childFragmentManager, CartFragment::class.java.simpleName)
        }
        cvAddress.setOnClickListener {
            if (mPermissionManager.areAllPermissionsGranted())
                navigateById(R.id.action_cartFragment_to_selectAddressFragment)
            else
                mPermissionManager.requestAllPermissions()
        }
        tvPlaceOrder.setOnClickListener {
            placeOrder()
        }
        cvContactDetails.setOnClickListener {
            contactDetailsDialog.show(childFragmentManager, CartFragment::class.java.simpleName)
        }
    }

    private fun placeOrder() {

        if (repoPrefs.getContactDetails() == null || repoPrefs.getContactDetails()?.name.isNullOrEmpty())
            showToast("Please enter contact details")
        else if (repoPrefs.getAddress() == null || repoPrefs.getAddress()?.line1.isNullOrEmpty())
            showToast("Please select address")
        else if (mViewModel.obsCartCount.value == null || mViewModel.obsCartCount.value == 0)
            showToast("Not items in the cart")
        else {

            mViewModel.placeOrder()

        }

    }


    override fun setupObservers(): CartViewModel.() -> Unit = {


        obsItemsList.observe(viewLifecycleOwner, Observer {
            mBinding.swipeRefreshLayout.isRefreshing = false
            if (it.isNullOrEmpty()) {
                mBinding.rvCartItems.hide()
            } else {
                mBinding.rvCartItems.show()
                mCartAdapter.submitList(it)
            }

        })

        mViewModel.obsPlaceOrderResponse.observe(viewLifecycleOwner, Observer { response ->
            if (response.status == Order.STATUS_CREATED) {

                    mViewModel.clearCartItems()
                    repoPrefs.saveSelectedOrder(response)
                    navigateById(R.id.action_cartFragment_to_orderFragment)

            }

        })

    }

    override fun onItemChanged(cartItem: CartItem, price: Int, type: Boolean) {
        mViewModel.updateItemNumber(cartItem, price, type)
    }

    override fun onResume() {
        mViewModel.getDeliveryAddress()
        super.onResume()
    }


    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        var arePermissionsGranted = true
        if (requestCode == PERMISSION_REQUEST_CODE) {
            grantResults.forEach { if (it != PackageManager.PERMISSION_GRANTED) arePermissionsGranted = false }
            if (!arePermissionsGranted) {
                snackbarShow = true
                showSettingSnackBar()
            }
            navigateById(R.id.action_cartFragment_to_selectAddressFragment)
        }
    }

    private fun showSettingSnackBar() {
        if (snackbarShow)
            Snackbar.make(requireView(), "Please give location permission for better user experience", Snackbar.LENGTH_LONG)
                .setAction("Settings") {
                    val intent = Intent()
                    intent.action = Settings.ACTION_APPLICATION_DETAILS_SETTINGS
                    val uri = Uri.fromParts("package",
                        BuildConfig.APPLICATION_ID, null)
                    intent.data = uri
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                    startActivity(intent)
                }
                .show()
    }

}
