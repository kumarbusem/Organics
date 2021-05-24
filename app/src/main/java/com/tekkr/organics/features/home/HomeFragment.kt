package com.tekkr.organics.features.home

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.tekkr.data.roomDatabase.BigItem
import com.tekkr.data.roomDatabase.CartItem
import com.tekkr.data.roomDatabase.Item
import com.tekkr.organics.R
import com.tekkr.organics.common.BaseAbstractFragment
import com.tekkr.organics.common.ViewModelFactory
import com.tekkr.organics.common.hide
import com.tekkr.organics.common.show
import com.tekkr.organics.databinding.FragmentHomeBinding

class HomeFragment : BaseAbstractFragment<HomeViewModel, FragmentHomeBinding>(R.layout.fragment_home), ItemsListAdapter.ItemCallback {


    private val mVegetablesAdapter: ItemsListAdapter by lazy {
        ItemsListAdapter(this@HomeFragment)
    }
    private val mFruitsAdapter: ItemsListAdapter by lazy {
        ItemsListAdapter(this@HomeFragment)
    }
    private val mMeatAdapter: ItemsListAdapter by lazy {
        ItemsListAdapter(this@HomeFragment)
    }

    override fun setViewModel(): HomeViewModel =
            ViewModelProvider(this@HomeFragment, ViewModelFactory {
                HomeViewModel(requireActivity().application)
            }).get(HomeViewModel::class.java)

    override fun setupViews(): FragmentHomeBinding.() -> Unit = {
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


    }


    override fun setupObservers(): HomeViewModel.() -> Unit = {

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

    override fun onItemChanged(cartItem: CartItem, type: Boolean) {
        mViewModel.updateItemNumber(cartItem, type)
    }


}
