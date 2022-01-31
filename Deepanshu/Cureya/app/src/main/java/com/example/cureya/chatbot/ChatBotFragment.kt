package com.example.cureya

import android.os.Bundle
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.android.volley.AuthFailureError
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.cureya.adapter.MessageListAdapter
import com.example.cureya.databinding.FragmentChatbotBinding
import com.example.cureya.model.messageInfo

class ChatBotActivity : Fragment() {

    private lateinit var binding: FragmentChatbotBinding
    private lateinit var madapter: MessageListAdapter
    private var message = ""    // message to be sent
    private var url = ""        // api url
    private var sender= ""      // sender name

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChatbotBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

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
                // madapter.updatemessage(newsArray)
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

    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity).supportActionBar?.show()
    }

    /**
     * In terms fragments, we have to use findNavController()
     * to navigate through screens
     */
    private fun emoji() {

    }

    private fun attach() {
        // val intent = Intent()
        // intent.type = "*/*"
        /* intent.action = Intent.ACTION_GET_CONTENT
        intent.putExtra("return-data", true)
        startActivityForResult(Intent.createChooser(intent, "Complete action using"), pickFromGallery) */
    }

    private fun camera() {
        /* val intent = Intent("android.media.action.IMAGE_CAPTURE")
        startActivity(intent) */
    }

    private fun mic() {

    }

    /* override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == pickFromGallery && resultCode == RESULT_OK) {
            if (data != null) {
                url = data.data?.toString()!!
            }
        }
    } */

    companion object {
        private val REQUEST_CODE = 200
        private val PICK_UP_FROM_GALLERY = 101
    }
}