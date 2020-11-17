package com.example.roomretrofitoffline

import android.app.Application
import com.example.roomretrofitoffline.repositary.module
import com.example.roomretrofitoffline.repositary.viewModel
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class RoomRetrofitOffline :Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin{
            androidContext(this@RoomRetrofitOffline)
            modules(module,viewModel)
        }
    }
}