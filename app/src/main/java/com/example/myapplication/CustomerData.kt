package com.example.myapplication

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass


@JsonClass(generateAdapter = true)
data class Customer (
    @Json(name = "id")
    var id: String,
    @Json(name = "name")
    var name: String,
    @Json(name="email")
    var email: String

)
