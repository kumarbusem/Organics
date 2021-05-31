package com.tekkr.organics.common

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.tekkr.data.dataSources.definitions.DataSourceImage
import com.tekkr.data.dataSources.definitions.DataSourceBasic
import com.tekkr.data.dataSources.definitions.DataSourceSharedPreferences
import com.tekkr.data.dataSources.definitions.DataSourceUser
import com.tekkr.data.dataSources.repos.RepoImage
import com.tekkr.data.dataSources.repos.RepoBasic
import com.tekkr.data.dataSources.repos.RepoSharedPreferences
import com.tekkr.data.dataSources.repos.RepoUser
import com.tekkr.data.internal.common.ApiException
import com.tekkr.data.models.SimpleResponse
import com.tekkr.data.roomDatabase.TekkrRoomDatabase
import com.tekkr.data.roomDatabase.TekkrRoomRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import java.net.SocketTimeoutException

open class BaseViewModel(context: Application) : AndroidViewModel(context) {

    val obsIsDataLoading: MutableLiveData<Boolean> = MutableLiveData()
    val obsMessage = MutableLiveData<String>()
    var isUserLogout = MutableLiveData<Boolean>()
    val obsIsUserAuthenticated: MutableLiveData<Boolean> = MutableLiveData()

    protected val TAG: String = javaClass.simpleName

    private val mJob: Job by lazy { Job() }
    protected val ioScope: CoroutineScope by lazy { CoroutineScope(Dispatchers.IO + mJob) }
    val roomDatabase by lazy { TekkrRoomDatabase.getDatabase(context) }

    protected val repoPrefs: DataSourceSharedPreferences by lazy { RepoSharedPreferences() }
    protected val repoUser: DataSourceUser by lazy { RepoUser() }
    protected val repoBasic: DataSourceBasic by lazy { RepoBasic() }
    protected val repoImage: DataSourceImage by lazy { RepoImage() }

    val roomRepository by lazy { TekkrRoomRepository(roomDatabase.itemsDao(), roomDatabase.addressesDao()) }

    override fun onCleared() {
        super.onCleared()
        if (this::mJob.isLazyInitialized) mJob.cancel()
    }


    fun getUser() {
        val user = repoPrefs.getLoggedInUser()
        obsIsUserAuthenticated.postValue(user != null && !user.access.isEmpty())
    }

    fun verifyOtp(phone: String, otp: String, res: (SimpleResponse) -> Unit) {
        ioScope.launch {
            try {
                repoUser.verifyOTP(phone, otp) { user ->
                    if (user == null || user.access.isEmpty())
                        res(SimpleResponse(SimpleResponse.STATUS_FAILED, "Access token not available"))
                    else {
                        repoPrefs.saveLoggedInUser(user)
                        obsIsUserAuthenticated.postValue(true)
                        res(SimpleResponse(SimpleResponse.STATUS_SUCCESS, ""))
                    }
                }
            } catch (e: ApiException) {
                Log.e("API:::", e.printStackTrace().toString())
                res(SimpleResponse(SimpleResponse.STATUS_FAILED, e.message.toString()))
            } catch (e: SocketTimeoutException) {
                res(SimpleResponse(SimpleResponse.STATUS_FAILED, "Slow Network!\nPlease ty again"))
            } catch (e: Exception) {
                if (e.message.toString().contains("Unable to resolve"))
                    res(SimpleResponse(SimpleResponse.STATUS_FAILED, "Network Issue\nUnable to resolve host"))
                else res(SimpleResponse(SimpleResponse.STATUS_FAILED, e.message.toString()))
                Log.e("EXC:::", e.printStackTrace().toString())
            }
        }
    }

    fun sendOtp(phone: String, res: (SimpleResponse) -> Unit) {
        ioScope.launch {
            try {
                repoUser.sendOTP(phone) { responce ->
                    res(responce)
                }
            } catch (e: ApiException) {
                res(SimpleResponse(SimpleResponse.STATUS_FAILED, e.message.toString()))
            } catch (e: SocketTimeoutException) {
                res(SimpleResponse(SimpleResponse.STATUS_FAILED, "Slow Network!\nPlease ty again"))
            } catch (e: Exception) {
                if (e.message.toString().contains("Unable to resolve"))
                    res(SimpleResponse(SimpleResponse.STATUS_FAILED, "Network Issue\nUnable to resolve host"))
                else res(SimpleResponse(SimpleResponse.STATUS_FAILED, e.message.toString()))
            }
        }
    }
}