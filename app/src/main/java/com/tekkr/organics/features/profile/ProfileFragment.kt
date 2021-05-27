package com.tekkr.organics.features.profile

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
import com.tekkr.organics.databinding.FragmentProfileBinding
import com.tekkr.organics.features.dialogs.OTPDialog

class ProfileFragment : BaseAbstractFragment<ProfileViewModel, FragmentProfileBinding>(R.layout.fragment_profile) {

    override fun setViewModel(): ProfileViewModel =
            ViewModelProvider(this@ProfileFragment, ViewModelFactory {
                ProfileViewModel(requireActivity().application)
            }).get(ProfileViewModel::class.java)

    override fun setupViews(): FragmentProfileBinding.() -> Unit = {

        btnBack.setOnClickListener {
            navigateBack()
        }
        btnLogout.setOnClickListener {
            repoPrefs.clearLoggedInUser()
            navigateBack()
        }

    }

    override fun setupObservers(): ProfileViewModel.() -> Unit = {


    }

}
