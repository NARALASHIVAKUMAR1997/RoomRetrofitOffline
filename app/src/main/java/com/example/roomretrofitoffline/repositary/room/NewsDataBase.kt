package com.example.roomretrofitoffline.repositary.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [News::class], version = 1,exportSchema = false)
abstract class NewsDataBase : RoomDatabase() {
    abstract fun newsDao(): NewsDao
}