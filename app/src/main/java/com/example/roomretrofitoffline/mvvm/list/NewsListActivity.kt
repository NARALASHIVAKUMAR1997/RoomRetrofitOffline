package com.example.roomretrofitoffline.mvvm.list

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.roomretrofitoffline.R
import com.example.roomretrofitoffline.mvvm.details.NewsDetailsActivity
import com.example.roomretrofitoffline.repositary.State
import kotlinx.android.synthetic.main.activity_news_list.*
import org.koin.android.ext.android.getKoin
import org.koin.java.KoinJavaComponent

class NewsListActivity : AppCompatActivity() {

    val newsListViewModel: NewsListViewModel by getKoin().inject()
    lateinit var newsAdapter: NewsAdapter
    lateinit var linearLayoutManager: LinearLayoutManager
    private val VISIBLE_THRESHOLD = 1
    private var pageNumber: Int = 1
    private var limitNumber = 20
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_list)
        callNavigationToDbOrServer()
        retryConnection()
        setUpRecyclerView()
        setUpObservers()
        setUpMoreListener()

    }

    private fun setUpMoreListener() {
        recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val totalItemCount = linearLayoutManager.itemCount
                val lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition()
                if (!newsListViewModel.progressBarLiveData.value!! && totalItemCount <= (lastVisibleItem + VISIBLE_THRESHOLD)) {
                    if (newsListViewModel.netWorkState.value == State.MOBILE_NETWORK_ON) {
                        pageNumber++
                        newsListViewModel.pageNumber.value = pageNumber
                        newsListViewModel.publishProcessor.onNext(pageNumber)
                        newsListViewModel.progressBarLiveData.value = true
                    } else {
                        limitNumber += 20
                        newsListViewModel.limitValue.value = limitNumber
                    }


                }
            }
        })
    }

    private fun callNavigationToDbOrServer() {
        newsListViewModel.navigationToDbOrServer()
    }

    private fun navigateToNewsDetailActivity(view: View, position: Int) {
        val intent = Intent(this, NewsDetailsActivity::class.java)
        val list = newsListViewModel.newsLiveData.value?.get(position)
        intent.putExtra("news", list!!)
        startActivity(intent)

    }

    private fun setUpRecyclerView() {
        linearLayoutManager = LinearLayoutManager(
            this@NewsListActivity,
            LinearLayoutManager.VERTICAL, false
        )
        newsAdapter = NewsAdapter(this@NewsListActivity, emptyList()) { view, position ->
            navigateToNewsDetailActivity(view, position)
        }
        recyclerView.apply {
            this.layoutManager = linearLayoutManager
            this.adapter = newsAdapter
        }
    }

    private fun setUpObservers() {
        newsListViewModel.newsLiveData.observe(this, Observer {
            newsAdapter.update(it)
        })

        newsListViewModel.netWorkState.observe(this, Observer {
            if (it == State.MOBILE_NETWORK_OFF_NO_DATA_DB) {
                retry_text.visibility = View.VISIBLE
                retry_btn.visibility = View.VISIBLE
                progress_horizontal.visibility = View.GONE
            } else {
                retry_text.visibility = View.GONE
                retry_btn.visibility = View.GONE
                progress_horizontal.visibility = View.VISIBLE
            }
        })
        newsListViewModel.progressBarLiveData.observe(this, Observer {
            if (!it) {
                progress_horizontal.visibility = View.GONE
            } else {
                progress_horizontal.visibility = View.VISIBLE
            }
        })
    }

    private fun retryConnection() {
        retry_btn.setOnClickListener {
            callNavigationToDbOrServer()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        newsListViewModel.clearDisposable().clear()
    }
}