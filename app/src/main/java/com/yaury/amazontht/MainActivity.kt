package com.yaury.amazontht

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
const val BASE_URL = "https://fetch-hiring.s3.amazonaws.com/"

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        getMyItems()
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    private fun getMyItems() {
        val retrofitBuilder = Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(ItemService::class.java)
        val retrofitItem = retrofitBuilder.getItem()

        retrofitItem.enqueue(object : Callback<List<ItemsItem>?> {
            override fun onResponse(call: Call<List<ItemsItem>?>, response: Response<List<ItemsItem>?>) {
                var responseBody = response.body()!!
                    .filter { !it.name.isNullOrBlank() }
                    .sortedBy { it.name.replace("Item ", "").toInt() }
                    .sortedBy { it.listId }

                val sb = StringBuilder()
                for (myItems in responseBody){
                    sb.append("Id: ${myItems.id} \n")
                    sb.append("List Id: ${myItems.listId} \n")
                    sb.append("Name: ${myItems.name} \n\n\n")
                }
                val myText = findViewById<TextView>(R.id.txt)
                myText.text = sb
            }

            override fun onFailure(p0: Call<List<ItemsItem>?>, p1: Throwable) {
                Log.e("MyTag", "On failure: " + p1.message)
            }
        })
    }
}