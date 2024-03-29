package com.tekkr.organics.features.order

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.tekkr.data.models.Customer
import com.tekkr.data.roomDatabase.CartItem
import com.tekkr.organics.R
import com.tekkr.organics.common.*
import com.tekkr.organics.databinding.FragmentCartBinding
import com.tekkr.organics.databinding.FragmentOrderBinding
import com.tekkr.organics.features.cart.CartListAdapter
import com.tekkr.organics.features.dialogs.ContactDetailsDialog


class OrderFragment : BaseAbstractFragment<OrderViewModel, FragmentOrderBinding>(R.layout.fragment_order), CartListAdapter.ItemCallback{

    private val mPermissionManager: PermissionManager by lazy { PermissionManager(this@OrderFragment) }
    private val mOrderAdapter: CartListAdapter by lazy {
        CartListAdapter(this@OrderFragment, CartListAdapter.TYPE_ORDER)
    }

    override fun setViewModel(): OrderViewModel =
            ViewModelProvider(this@OrderFragment, ViewModelFactory {
                OrderViewModel(requireActivity().application)
            }).get(OrderViewModel::class.java)

    override fun setupViews(): FragmentOrderBinding.() -> Unit = {

        rvCartItems.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = mOrderAdapter
        }

        btnMakePayment.setOnClickListener {
//            showInfoDialogueFor("Payment", "Do not press back button while making payment", "Don't close the application", "PAY", true){
//                navigateById(R.id.action_orderFragment_to_paymentFragment)
//            }
            showInfoDialogueFor("Sorry for the inconvenience", "Currently we are not accepting any orders", "Items not available", "Okay", true){

            }
        }

        btnBack.setOnClickListener {
            navigateBack()
        }

        btnHelp.setOnClickListener {
            helpDialog.show(childFragmentManager, OrderFragment::class.java.simpleName)
        }

    }


    override fun setupObservers(): OrderViewModel.() -> Unit = {


        obsItemsList.observe(viewLifecycleOwner, Observer {
            if (it.isNullOrEmpty()) {
                mBinding.rvCartItems.hide()
            } else {
                mBinding.rvCartItems.show()
                mOrderAdapter.submitList(it)
            }

        })
    }

    override fun onItemChanged(cartItem: CartItem, price: Int, type: Boolean) {

    }

    override fun onResume() {
        mViewModel.getOrder()
        super.onResume()
    }




}
