package com.tekkr.organics.features.profile

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tekkr.data.models.Order
import com.tekkr.data.roomDatabase.CartItem
import com.tekkr.organics.R
import com.tekkr.organics.common.BaseAbstractFragment
import com.tekkr.organics.common.ViewModelFactory
import com.tekkr.organics.common.hide
import com.tekkr.organics.common.show
import com.tekkr.organics.databinding.FragmentCartBinding
import com.tekkr.organics.databinding.FragmentProfileBinding
import com.tekkr.organics.features.cart.CartListAdapter
import com.tekkr.organics.features.dialogs.OTPDialog

class ProfileFragment : BaseAbstractFragment<ProfileViewModel, FragmentProfileBinding>(R.layout.fragment_profile), OrderAdapter.ItemCallback{

    private val mOrderAdapter: OrderAdapter by lazy {
        OrderAdapter(this@ProfileFragment)
    }

    override fun setViewModel(): ProfileViewModel =
            ViewModelProvider(this@ProfileFragment, ViewModelFactory {
                ProfileViewModel(requireActivity().application)
            }).get(ProfileViewModel::class.java)

    override fun setupViews(): FragmentProfileBinding.() -> Unit = {

        rvOrders.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mOrderAdapter
        }

        btnBack.setOnClickListener {
            navigateBack()
        }
        btnLogout.setOnClickListener {
            showInfoDialogueFor("Logout", "Do you want to Logout", "", "Yes", false) {
                repoPrefs.clearLoggedInUser()
                navigateBack()
            }
        }

        srl.setOnRefreshListener {
            mViewModel.getOrders()
        }

    }

    override fun setupObservers(): ProfileViewModel.() -> Unit = {
        obsOrdersList.observe(viewLifecycleOwner, Observer {
            mBinding.srl.isRefreshing = false
            if (it.isNullOrEmpty()) {
                mBinding.rvOrders.hide()
                mBinding.tvSrl.show()
            } else {
                mBinding.rvOrders.show()
                mBinding.tvSrl.hide()
                mOrderAdapter.submitList(it)
            }
        })

    }

    override fun onOrderSelected(order: Order) {
        repoPrefs.saveSelectedOrder(order)
        navigateById(R.id.action_profileFragment_to_orderFragment)
    }

    override fun onResume() {
        mViewModel.getOrders()
        super.onResume()
    }

}
