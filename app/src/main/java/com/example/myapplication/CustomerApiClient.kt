package com.example.myapplication


import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.*

interface CustomerApiClient{
    @GET("user") fun getPartsAsync(): Deferred<Response<List<Customer>>>
    @POST("users") fun addPartAsync(@Body newPart : Customer): Deferred<Response<Void>>
    @DELETE("users/{id}") fun deletePartAsync(@Path("id") id: String) : Deferred<Response<Void>>
    @PUT("users/{id}") fun updatePartAsync(@Path("id") id: String, @Body newPart: Customer) : Deferred<Response<Void>>
}