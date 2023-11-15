package com.reto1.mytubeapp.data.repository.remote

import android.util.Log
import com.reto1.mytubeapp.MyTube
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    const val API_URI = "http://10.0.2.2:8080/"

    private val client = OkHttpClient.Builder().addInterceptor { chain ->
      val authToken=MyTube.userPreferences.fetchAuthToken()
      val newRequest: Request = chain.request().newBuilder()
          .addHeader("Authorization", "Bearer $authToken")
          .build()
        chain.proceed(newRequest)
    } .build()


    val retrofitClient: Retrofit.Builder by lazy {
        Retrofit.Builder()
            .client(client)
            .baseUrl(API_URI)
            .addConverterFactory(GsonConverterFactory.create())
    }

    val apiInterface: APIInterface by lazy {
        retrofitClient
            .build()
            .create(APIInterface::class.java)
    }


}