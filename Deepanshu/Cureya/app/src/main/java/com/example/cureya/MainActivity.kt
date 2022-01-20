package com.example.cureya

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley

class MainActivity : AppCompatActivity() {
    private lateinit var madapter: MessageListAdapter
    private var message =""    // message to be sent
    private var url = ""        // api url
    private var sender=""       // sender name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        url = "TODO"     // Add API HERE.
        fetchData()
        madapter = MessageListAdapter()

    }
    private fun fetchData()  {

        val queue = Volley.newRequestQueue(this)
        val getRequest: JsonObjectRequest = object : JsonObjectRequest(
            Request.Method.GET,
            url,
            null,
            Response.Listener{

                val chatJsonArray = it.getJSONArray("articles")
                val messageArray = ArrayList<messageInfo>()
                for(i in 0 until chatJsonArray.length()){
                    val chatJsonObject=  chatJsonArray.getJSONObject(i)
                    val message = messageInfo(
                        chatJsonObject.getString("title"),
                        chatJsonObject.getString("author"),
//                        chatJsonObject.getString("url"),
//                        chatJsonObject.getString("urlToImage"),
//                        chatJsonObject.getString("description")
                    )
                    messageArray.add(message)
                }
                //madapter.updatemessage(newsArray)
            },
            Response.ErrorListener { error ->

            }
        ) {
            @Throws(AuthFailureError::class)
            override fun getHeaders(): Map<String, String> {
                val params: MutableMap<String, String> = HashMap()
                params["User-Agent"] = "Mozilla/5.0"
                return params
            }
        }

        queue.add(getRequest)
    }
}