package com.example.roomretrofitoffline.mvvm.details

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.roomretrofitoffline.R
import com.example.roomretrofitoffline.repositary.room.News
import kotlinx.android.synthetic.main.activity_news_details.*

class NewsDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_details)
        val news = intent.getSerializableExtra("news") as News
        time_stamp.text = news.NewsTimeStamp
        title_news.text = news.title
        description.text = news.des
        content.text = news.content


    }
}