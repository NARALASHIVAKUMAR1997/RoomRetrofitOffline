package com.example.roomretrofitoffline.repositary

import android.content.Context
import android.util.Log
import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.roomretrofitoffline.repositary.network.ApiInterface
import com.example.roomretrofitoffline.repositary.room.DataBaseBuilder.getnewsDataBase
import com.example.roomretrofitoffline.repositary.room.News
import com.example.roomretrofitoffline.repositary.room.NewsDao
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import io.reactivex.observers.DisposableObserver
import io.reactivex.processors.PublishProcessor
import io.reactivex.schedulers.Schedulers
import retrofit2.Retrofit
import org.koin.java.KoinJavaComponent.getKoin
import java.util.Collections.addAll

class NewsRepository(private val context: Context) {
    private val retrofit: Retrofit by getKoin().inject()
    private lateinit var apiInterface: ApiInterface
    private val compositeDisposable = CompositeDisposable()
    private var newsDao: NewsDao = getnewsDataBase(context).newsDao()
    private val apiKey = "c0e95ca6175d4a48b0ed98f7ff97f346"


    fun getDataFromServer(
        list: MutableLiveData<ArrayList<News>>,
        progressBarLiveData: MutableLiveData<Boolean>,
        publishProcessor: PublishProcessor<Int>,
        pageNumber: Int
    ):
            Pair<MutableLiveData<ArrayList<News>>, MutableLiveData<Boolean>> {
        apiInterface = retrofit.create(ApiInterface::class.java)
        val disposable = publishProcessor.onBackpressureDrop()
            .doOnNext {
                progressBarLiveData.value = true
            }
            .concatMapSingle {
                apiInterface.getNewsDetail("de", "science", apiKey, it, 20)
                    .subscribeOn(Schedulers.io())
                    .doOnError {}
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                for (elements in it.articles.indices) {
                    newsDao.insertAll(it.articles[elements])
                    if (list.value == null) {
                        list.value = it.articles
                    } else {
                        list.value!!.add(it.articles[elements])
                    }
                }
                progressBarLiveData.value = false
            }
        compositeDisposable.add(disposable)
        publishProcessor.onNext(pageNumber)
        return Pair(list, progressBarLiveData)
    }

    fun getDataFromRoom(
        list: MutableLiveData<ArrayList<News>>,
        progressBarLiveData: MutableLiveData<Boolean>,
        publishProcessor: PublishProcessor<Int>,
        pageNumber: Int
    ):
            Pair<MutableLiveData<ArrayList<News>>, MutableLiveData<Boolean>> {
        val disposable = publishProcessor.onBackpressureDrop()
            .doOnNext {
                progressBarLiveData.value = true
            }
            .concatMapSingle {
                newsDao.getnewsData(pageNumber)
                    .subscribeOn(Schedulers.io())
                    .doOnError {}
            }
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe {
                for (elements in it.indices) {
                    if (list.value == null) {
                        list.value = it as ArrayList<News>?
                    } else {
                        list.value!!.add(it[elements])
                    }
                }
                progressBarLiveData.value = false
            }
        compositeDisposable.add(disposable)
        publishProcessor.onNext(pageNumber)
        return Pair(list, progressBarLiveData)
    }

    fun getCountOfDbRecords(): Int {
        return newsDao.getCount()
    }

    fun returnCompositeDisposable(): CompositeDisposable {
        return compositeDisposable
    }

}