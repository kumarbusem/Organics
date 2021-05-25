package com.tekkr.organics.features.shop

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.tekkr.data.roomDatabase.CartItem
import com.tekkr.organics.R
import com.tekkr.organics.common.BaseAbstractFragment
import com.tekkr.organics.common.ViewModelFactory
import com.tekkr.organics.common.hide
import com.tekkr.organics.common.show
import com.tekkr.organics.databinding.FragmentShopBinding

class ShopFragment : BaseAbstractFragment<ShopViewModel, FragmentShopBinding>(R.layout.fragment_shop), ItemsListAdapter.ItemCallback {


    private val mVegetablesAdapter: ItemsListAdapter by lazy {
        ItemsListAdapter(this@ShopFragment)
    }
    private val mFruitsAdapter: ItemsListAdapter by lazy {
        ItemsListAdapter(this@ShopFragment)
    }
    private val mMeatAdapter: ItemsListAdapter by lazy {
        ItemsListAdapter(this@ShopFragment)
    }

    override fun setViewModel(): ShopViewModel =
            ViewModelProvider(this@ShopFragment, ViewModelFactory {
                ShopViewModel(requireActivity().application)
            }).get(ShopViewModel::class.java)

    override fun setupViews(): FragmentShopBinding.() -> Unit = {
        rvVegetables.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = mVegetablesAdapter
        }
        rvFruits.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = mFruitsAdapter
        }
        rvMeat.apply {
            layoutManager = GridLayoutManager(requireContext(), 2)
            adapter = mMeatAdapter
        }

        swipeRefreshLayout.setOnRefreshListener {
            mViewModel.getItems()
        }

        cvCart.setOnClickListener { navigateById(R.id.action_shopFragment_to_cartFragment) }

    }


    override fun setupObservers(): ShopViewModel.() -> Unit = {

        obsVegetablesList.observe(viewLifecycleOwner, Observer {
            if(it.isNullOrEmpty()){
                mBinding.ivVegetables.show()
                mBinding.rvVegetables.hide()
            }else{
                mBinding.ivVegetables.hide()
                mBinding.rvVegetables.show()
                mVegetablesAdapter.submitList(it)
            }
            mBinding.swipeRefreshLayout.isRefreshing = false

        })
        obsFruitsList.observe(viewLifecycleOwner, Observer {
            if(it.isNullOrEmpty()){
                mBinding.ivFruits.show()
                mBinding.rvFruits.hide()
            }else{
                mBinding.ivFruits.hide()
                mBinding.rvFruits.show()
                mFruitsAdapter.submitList(it)
            }

        })
        obsMeatList.observe(viewLifecycleOwner, Observer {
            if(it.isNullOrEmpty()){
                mBinding.ivMeat.show()
                mBinding.rvMeat.hide()
            }else{
                mBinding.ivMeat.hide()
                mBinding.rvMeat.show()
                mMeatAdapter.submitList(it)
            }

        })


    }

    override fun onItemChanged(cartItem: CartItem, price: Int, type: Boolean) {
        mViewModel.updateItemNumber(cartItem, price, type)
    }

    override fun onResume() {
        mViewModel.getItems()
        super.onResume()
    }
}