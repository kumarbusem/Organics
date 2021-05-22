package com.tekkr.organics.features.home

import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.tekkr.data.roomDatabase.Item
import com.tekkr.organics.R
import com.tekkr.organics.common.BaseAbstractFragment
import com.tekkr.organics.common.ViewModelFactory
import com.tekkr.organics.databinding.FragmentHomeBinding

class HomeFragment : BaseAbstractFragment<HomeViewModel, FragmentHomeBinding>(R.layout.fragment_home), ItemsListAdapter.ItemSelectionCallback {


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
            mVegetablesAdapter.submitList(it)
            mBinding.swipeRefreshLayout.isRefreshing = false
        })
        obsFruitsList.observe(viewLifecycleOwner, Observer {
            mFruitsAdapter.submitList(it)
        })
        obsMeatList.observe(viewLifecycleOwner, Observer {
            mMeatAdapter.submitList(it)
        })


    }

    override fun onItemselected(Items: Item) {

    }

}
