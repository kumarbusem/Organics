package com.tekkr.organics.features.cart

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tekkr.data.models.ContactDetails
import com.tekkr.data.roomDatabase.CartItem
import com.tekkr.organics.R
import com.tekkr.organics.common.*
import com.tekkr.organics.databinding.FragmentCartBinding
import com.tekkr.organics.features.dialogs.ContactDetailsDialog
import com.tekkr.organics.features.dialogs.OTPDialog

class CartFragment : BaseAbstractFragment<CartViewModel, FragmentCartBinding>(R.layout.fragment_cart), CartListAdapter.ItemCallback {


    private val mPermissionManager: PermissionManager by lazy { PermissionManager(this@CartFragment) }
    private val mCartAdapter: CartListAdapter by lazy {
        CartListAdapter(this@CartFragment)
    }

    private val contactDetailsDialog: ContactDetailsDialog by lazy {
        ContactDetailsDialog(onSaveContactDetails = { name, phone ->
            repoPrefs.saveContactDetails(ContactDetails(name, phone))
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
        else if (repoPrefs.getAddress() == null || repoPrefs.getAddress()?.line2.isNullOrEmpty())
            showToast("Please select address")
        else if (mViewModel.obsCartCount.value == null || mViewModel.obsCartCount.value == 0)
            showToast("Not items in the cart")
        else {

            repoPrefs.clearTempAddress()
            repoPrefs.clearAddress()

            navigateBack()
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
    }

    override fun onItemChanged(cartItem: CartItem, price: Int, type: Boolean) {
        mViewModel.updateItemNumber(cartItem, price, type)
    }

    override fun onResume() {
        mViewModel.getDeliveryAddress()
        super.onResume()
    }

}
