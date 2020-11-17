package com.example.roomretrofitoffline.repositary.room

import androidx.lifecycle.LiveData
import androidx.room.*
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface NewsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(news: News)

    @Query("select * from newsTable LIMIT :limit")
    fun getnewsData(limit:Int): Single<List<News>>

    @Query("select COUNT(*) from newsTable")
    fun getCount():Int

}