package com.example.roomretrofitoffline.mvvm.list

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.roomretrofitoffline.repositary.NewsRepository
import com.example.roomretrofitoffline.repositary.State
import com.example.roomretrofitoffline.repositary.room.News
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.processors.PublishProcessor
import org.koin.java.KoinJavaComponent.getKoin

class NewsListViewModel(val context: Context) : ViewModel() {
    val newsRepositary: NewsRepository by getKoin().inject()
    var newsLiveData = MutableLiveData<ArrayList<News>>()
    var progressBarLiveData = MutableLiveData<Boolean>()
    lateinit var pair: Pair<MutableLiveData<ArrayList<News>>, MutableLiveData<Boolean>>
    var netWorkState = MutableLiveData<State>()
    val publishProcessor = PublishProcessor.create<Int>()
    val pageNumber = MutableLiveData<Int>()
    var limitValue = MutableLiveData<Int>()

    private fun checkWhetherDeviceOnlineOrOffline(): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val nw = connectivityManager.activeNetwork ?: return false
            val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
            return when {
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                else -> false
            }
        } else {
            val netInfo = connectivityManager.activeNetworkInfo ?: return false
            return netInfo.isConnected
        }
    }

    fun navigationToDbOrServer() {
        if (pageNumber.value == null) {
            pageNumber.value = 1
        }
        if (checkWhetherDeviceOnlineOrOffline()) {
            netWorkState.value = State.MOBILE_NETWORK_ON
            publishProcessor.onNext(pageNumber.value)
            pair = newsRepositary.getDataFromServer(
                newsLiveData,
                progressBarLiveData,
                publishProcessor,
                pageNumber.value!!
            )
            newsLiveData = pair.first
            progressBarLiveData = pair.second
        } else if (checkWhetherDbisEmptyOrNot()==0 && !checkWhetherDeviceOnlineOrOffline()) {
            netWorkState.value = State.MOBILE_NETWORK_OFF_NO_DATA_DB
        } else {
            netWorkState.value = State.MOBILE_NETWORK_OFF
            if (limitValue.value ==null){
                limitValue.value =20
            }
            publishProcessor.onNext(pageNumber.value)
            pair = newsRepositary.getDataFromRoom(
                newsLiveData,
                progressBarLiveData,
                publishProcessor,
                limitValue.value!!
            )
            newsLiveData = pair.first
            progressBarLiveData = pair.second
            Log.v("datata",pair.second.value.toString())
        }
    }

    private fun checkWhetherDbisEmptyOrNot(): Int {
        return newsRepositary.getCountOfDbRecords()
    }

    fun clearDisposable(): CompositeDisposable {
        return newsRepositary.returnCompositeDisposable()
    }


}