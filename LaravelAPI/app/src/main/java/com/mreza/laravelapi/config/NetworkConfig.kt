
package com.example.laravelapi.config

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class NetworkConfig {
    val Base_URL:String = "http://192.168.1.7:8000/laravel/laravel-api/public/api"

    private fun  setOkHttp(): OkHttpClient{
        val interceptor = HttpLoggingInterceptor().setLevel(
            HttpLoggingInterceptor.Level.BASIC
        ).setLevel(HttpLoggingInterceptor.Level.BODY)

        return OkHttpClient.Builder()
            .addInterceptor(interceptor)
            .callTimeout(15L, TimeUnit.SECONDS)
            .build()
    }
    private fun setRetrofit():Retrofit{
        return Retrofit.Builder()
            .baseUrl(Base_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(setOkHttp())
            .build()
    }
    fun getServices():ApiServices{
        return setRetrofit().create(ApiServices::class.java)
    }
}
