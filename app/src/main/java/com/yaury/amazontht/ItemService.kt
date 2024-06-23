package com.yaury.amazontht

import retrofit2.Call
import retrofit2.http.GET

interface ItemService {

    @GET("hiring.json")
    fun getItem(): Call<List<ItemsItem>>
}