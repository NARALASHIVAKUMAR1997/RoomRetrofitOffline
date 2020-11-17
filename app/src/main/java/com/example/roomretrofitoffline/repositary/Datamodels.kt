package com.example.roomretrofitoffline.repositary

import android.content.Context
import com.example.roomretrofitoffline.repositary.room.News
import com.google.gson.annotations.SerializedName

data class RemainsData(
    @SerializedName("status")
    var status:String,
    @SerializedName("totalResults")
    var totalResults:Int,
    @SerializedName("articles")
    var articles:ArrayList<News>)

enum class State{
   MOBILE_NETWORK_OFF,MOBILE_NETWORK_ON,MOBILE_NETWORK_OFF_NO_DATA_DB
}