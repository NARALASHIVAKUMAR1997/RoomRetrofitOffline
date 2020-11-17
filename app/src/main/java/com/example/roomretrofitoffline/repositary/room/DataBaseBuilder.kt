package com.example.roomretrofitoffline.repositary.room

import android.content.Context
import androidx.room.Room

object DataBaseBuilder {

    var newsDataBase: NewsDataBase? = null
    fun getnewsDataBase(context: Context): NewsDataBase {
        if (newsDataBase == null) {
            synchronized(NewsDataBase::class.java) {
                newsDataBase = buildnewsDb(context)
            }
        }
        return newsDataBase!!

    }

    private fun buildnewsDb(context: Context) =
        Room.databaseBuilder(
            context.applicationContext,
            NewsDataBase::class.java,
            "news_database"
        ).allowMainThreadQueries().build()
}