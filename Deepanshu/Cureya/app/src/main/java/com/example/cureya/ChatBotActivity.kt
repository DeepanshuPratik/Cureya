package com.example.cureya

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.cureya.adapter.MessageListAdapter
import com.example.cureya.databinding.ActivityChatbotBinding
import com.example.cureya.model.messageInfo

class ChatBotActivity : AppCompatActivity() {

    private lateinit var binding: ActivityChatbotBinding
    private lateinit var madapter: MessageListAdapter
    private val REQUEST_CODE = 200
    private var message = ""    // message to be sent
    private var url = ""        // api url
    private var sender= ""      // sender name

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chatbot)

        binding = ActivityChatbotBinding.inflate(layoutInflater)
        madapter = MessageListAdapter()
        // Add API HERE.
        url = "TODO"
        fetchData()

        binding.apply {
            attach.setOnClickListener { attach() }
            emoji.setOnClickListener { emoji() }
            camera.setOnClickListener { camera() }
            mic.setOnClickListener { mic() }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == REQUEST_CODE && data != null){
            //data.extras?.get("data") as Bitmap
            // here we have to send it as message. It is left as we dont have API config.
        }
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
    private fun emoji(){

    }
    private fun attach(){

    }
    private fun camera(){
        val cameraIntent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(cameraIntent, REQUEST_CODE)
    }
    private fun mic(){

    }
}