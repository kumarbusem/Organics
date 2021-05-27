package com.tekkr.organics.common

import android.content.Context
import android.os.Bundle
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.tekkr.data.dataSources.definitions.DataSourceSharedPreferences
import com.tekkr.data.dataSources.repos.RepoSharedPreferences
import com.tekkr.organics.MainActivity
import com.tekkr.organics.features.dialogs.OTPDialog
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

open class BaseFragment : Fragment() {

    private lateinit var mActivity: MainActivity
    private val mJob: Job by lazy { Job() }
    protected val ioScope: CoroutineScope by lazy { CoroutineScope(Dispatchers.IO + mJob) }
    protected val uiScope: CoroutineScope by lazy { CoroutineScope(Dispatchers.Main + mJob) }

    protected val repoPrefs: DataSourceSharedPreferences by lazy { RepoSharedPreferences() }

    protected val TAG: String = javaClass.simpleName

    protected inline fun <T> LiveData<T>.startObserving(crossinline onChange: (T?) -> Unit) {
        this.observe(viewLifecycleOwner, Observer { onChange(it) })
    }

    protected fun navigateById(navigationId: Int) {
        findNavController().navigate(navigationId)
    }

    protected fun navigateBack() {
        findNavController().popBackStack()
    }


    protected fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    protected fun hideKeyBoard() {
        val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        if (requireActivity().currentFocus != null)
            imm.hideSoftInputFromWindow(requireActivity().currentFocus!!.windowToken, 0)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mActivity = context as MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        if (this::mJob.isLazyInitialized) mJob.cancel()
    }

}