package com.tekkr.organics.common

import android.app.Application
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
import com.tekkr.data.roomDatabase.TekkrRoomDatabase
import com.tekkr.data.roomDatabase.TekkrRoomRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

open class BaseViewModel(context: Application) : AndroidViewModel(context) {

    val obsIsDataLoading: MutableLiveData<Boolean> = MutableLiveData()
    val obsMessage = MutableLiveData<String>()
    var isUserLogout = MutableLiveData<Boolean>()

    protected val TAG: String = javaClass.simpleName

    private val mJob: Job by lazy { Job() }
    protected val ioScope: CoroutineScope by lazy { CoroutineScope(Dispatchers.IO + mJob) }
    val roomDatabase by lazy { TekkrRoomDatabase.getDatabase(context) }

    protected val repoPrefs: DataSourceSharedPreferences by lazy { RepoSharedPreferences() }
    protected val repoUser: DataSourceUser by lazy { RepoUser() }
    protected val repoBasic: DataSourceBasic by lazy { RepoBasic() }
    protected val repoImage: DataSourceImage by lazy { RepoImage() }

    val roomRepository by lazy { TekkrRoomRepository(roomDatabase.recentAddressDao()) }

    override fun onCleared() {
        super.onCleared()
        if (this::mJob.isLazyInitialized) mJob.cancel()
    }
}