package com.tekkr.organics.features.splash

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.tekkr.organics.R
import com.tekkr.organics.common.BaseFragment
import kotlinx.android.synthetic.main.fragment_splash.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashFragment : BaseFragment() {


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        return inflater.inflate(R.layout.fragment_splash, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        navigateNext()
    }

    private fun navigateNext() {

        uiScope.launch {
                delay(SPLASH_DURATION)
                checkUserAuth()
            }
    }

    private fun checkUserAuth() {
            navigateById(R.id.action_splashFragment_to_shopFragment)
    }

    companion object {
        private const val SPLASH_DURATION: Long = 2000
    }

    override fun onResume() {
        checkUserAuth()
        super.onResume()
    }

}
