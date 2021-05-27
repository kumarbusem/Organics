package com.tekkr.organics.features.cart

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tekkr.data.roomDatabase.CartItem
import com.tekkr.organics.R
import com.tekkr.organics.common.BaseAbstractFragment
import com.tekkr.organics.common.ViewModelFactory
import com.tekkr.organics.common.hide
import com.tekkr.organics.common.show
import com.tekkr.organics.databinding.FragmentCartBinding
import com.tekkr.organics.features.dialogs.OTPDialog

class CartFragment : BaseAbstractFragment<CartViewModel, FragmentCartBinding>(R.layout.fragment_cart), CartListAdapter.ItemCallback {


    private val mCartAdapter: CartListAdapter by lazy {
        CartListAdapter(this@CartFragment)
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
            navigateById(R.id.action_cartFragment_to_selectAddressFragment)
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

}
