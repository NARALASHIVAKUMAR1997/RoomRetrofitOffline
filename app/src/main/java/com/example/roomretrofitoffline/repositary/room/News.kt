package com.example.roomretrofitoffline.repositary.room

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import org.jetbrains.annotations.PropertyKey
import java.io.Serializable

@Entity(tableName = "newsTable")
data class News(
    @PrimaryKey(autoGenerate = true)
    var index :Int,
    @SerializedName("author")
    var author: String?,
    @SerializedName("title")
    var title: String?,
    @SerializedName("description")
    var des: String?,
    @SerializedName("url")
    var fullUrl: String?,
    @SerializedName("urlToImage")
    var urlToImage: String?,
    @SerializedName("publishedAt")
    var NewsTimeStamp: String,
    @SerializedName("content")
    var content:String?
):Serializable