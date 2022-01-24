package com.example.cureya.chatbot

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.cureya.MessageListAdapter
import com.example.cureya.R
import com.example.cureya.messageInfo


/**
 * A simple [Fragment] subclass.
 * Use the [ChatbotFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class ChatbotFragment : Fragment() {
    private lateinit var madapter: MessageListAdapter
    private var message = ""    // message to be sent
    private var url = ""        // api url
    private var sender = ""       // sender name


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        url = "TODO"     // Add API HERE.
        fetchData()
        madapter = MessageListAdapter()
        return inflater.inflate(R.layout.fragment_chatbot, container, false)

    }


    private fun fetchData()  {

        val queue = Volley.newRequestQueue(context)
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