package com.lacour.vincent.wificaresp8266.screen

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.GsonBuilder
import com.lacour.vincent.wificaresp8266.R
import com.lacour.vincent.wificaresp8266.constant.Constant
import com.lacour.vincent.wificaresp8266.storage.Preferences

import kotlinx.android.synthetic.main.activity_temperature.*
import okhttp3.*
import java.io.IOException

class Temperature : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_temperature)

        //HTTP CLIENT
        recyclerView_main.layoutManager = LinearLayoutManager(this)
        fetchJson()
    }

    fun fetchJson() {
        println("Attempting to Fetch JSON")

        val preferences = Preferences(this@Temperature)
        val url = "http://${preferences.getIpAddress()}:${preferences.getPortTemp()}/datos"
        //val url= "http://192.168.1.8:3000/datos"
        //val url = "http://192.168.1.5:3000/datos"
        val request = Request.Builder().url(url).build()

        val client = OkHttpClient()
        client.newCall(request).enqueue(object: Callback {
            override fun onResponse(call: Call, response: Response) {
                val body = response.body()?.string()
                println(body)

                val gson = GsonBuilder().create()

                val homeFeed = gson.fromJson(body, HomeFeed::class.java)

                runOnUiThread {
                    recyclerView_main.adapter = MainAdapter(homeFeed)
                }
            }

            override fun onFailure(call: Call, e: IOException) {
                println("Failed to execute request")
            }
        })
    }
}

class HomeFeed(val temp: List<Video> )
class Video(val medicion: String, val valor: String, val id: Int)