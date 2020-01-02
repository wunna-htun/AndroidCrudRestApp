package com.example.myapplication

import android.util.Log
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

var  BaseUrl=""

object CustomerAccess {
    val customerApi : CustomerApiClient by lazy {
        Log.d("WebAccess", "Creating retrofit client")
        val retrofit = Retrofit.Builder()
            // The 10.0.2.2 address routes request from the Android emulator
            // to the localhost / 127.0.0.1 of the host PC
            .baseUrl("https://15kmkstr86.execute-api.us-east-1.amazonaws.com/dev/")
            // Moshi maps JSON to classes
            .addConverterFactory(MoshiConverterFactory.create())
            // The call adapter handles threads
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

        // Create Retrofit client
        return@lazy retrofit.create(CustomerApiClient::class.java)
    }
}