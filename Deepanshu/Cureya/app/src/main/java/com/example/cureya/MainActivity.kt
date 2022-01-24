package com.example.cureya

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.cureya.adapter.MessageListAdapter
import com.example.cureya.model.messageInfo

class MainActivity : AppCompatActivity() {
    private lateinit var madapter: MessageListAdapter
    private var message =""    // message to be sent
    private var url = ""        // api url
    private var sender=""       // sender name
    private val pickFromGallery:Int = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        url = "TODO"     // Add API HERE.
        fetchData()
        madapter = MessageListAdapter()

        findViewById<ImageView>(R.id.attach).setOnClickListener {
            attach()
        }
        findViewById<ImageView>(R.id.emoji).setOnClickListener {
            emoji()
        }
        findViewById<ImageView>(R.id.camera).setOnClickListener {
            camera()
        }
        findViewById<ImageView>(R.id.mic).setOnClickListener {
            mic()
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
            Response.ErrorListener {

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
        val intent = Intent()
        intent.type = "*/*"
        intent.action = Intent.ACTION_GET_CONTENT
        intent.putExtra("return-data", true)
        startActivityForResult(Intent.createChooser(intent, "Complete action using"), pickFromGallery)
    }
    private fun camera(){
        val intent = Intent("android.media.action.IMAGE_CAPTURE")
        startActivity(intent)
    }
    private fun mic(){

    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == pickFromGallery && resultCode == RESULT_OK) {
            if (data != null) {
                url = data.data?.toString()!!
            }
        }
        /**
         * Work flow is currently at [ChatBotFragment]
         */
        val intent = Intent(this, ChatBotFragment::class.java)
        this.startActivity(intent)
    }
}