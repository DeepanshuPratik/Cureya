package com.example.cureya.chatbot

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cureya.R
import com.example.cureya.chatbot.Constants.OPEN_GOOGLE
import com.example.cureya.chatbot.Constants.OPEN_SEARCH
import com.example.cureya.chatbot.Constants.RECEIVE_ID
import com.example.cureya.chatbot.Constants.SEND_ID
import kotlinx.coroutines.*

class ChatbotFragment : Fragment() {

    private lateinit var rvmessages : RecyclerView
    private lateinit var etmessage : EditText
    private var messagesList = mutableListOf<Message>()
    private lateinit var adapter: MessagingAdapter
    private val botList = listOf("Peter", "Francesca", "Luigi", "Igor")

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //        recyclerView()
//        clickEvents()
//        val random = (0..3).random()
//        customBotMessage("Hello! Today you're speaking with ${botList[random]}, how may I help?")
        return inflater.inflate(R.layout.fragment_chatbot, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvmessages = view.findViewById<RecyclerView>(R.id.rv_messages)
        etmessage = view.findViewById<EditText>(R.id.et_message)
        recyclerView()
        clickEvents()
        val random = (0..3).random()
        customBotMessage("Hello! Today you're speaking with ${botList[random]}, how may I help?")
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun clickEvents() {

        //Send a message
        view?.findViewById<ImageView>(R.id.send_button)?.setOnClickListener {
            requireView().findViewById<TextView>(R.id.welcome).visibility= View.GONE
            requireView().findViewById<ImageView>(R.id.chatbot).visibility= View.GONE
            requireView().findViewById<TextView>(R.id.Intro).visibility= View.GONE
            sendMessage()
        }

        //Scroll back to correct position when user clicks on text view
        etmessage.setOnClickListener {
            GlobalScope.launch {
                delay(100)

                withContext(Dispatchers.Main) {
                    view?.findViewById<RecyclerView>(R.id.rv_messages)?.scrollToPosition(adapter.itemCount - 1)

                }
            }
        }
    }

    private fun recyclerView() {
        adapter = MessagingAdapter()
        rvmessages.adapter = adapter
        rvmessages.layoutManager = LinearLayoutManager(activity)

    }

    override fun onStart() {
        super.onStart()
        //In case there are messages, scroll to bottom when re-opening app
        GlobalScope.launch {
            delay(100)
            withContext(Dispatchers.Main) {
                rvmessages.scrollToPosition(adapter.itemCount - 1)
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun sendMessage() {
        val message = etmessage.text.toString()
        val timeStamp = Time.timeStamp()

        if (message.isNotEmpty()) {
            //Adds it to our local list

            messagesList.add(Message(message, SEND_ID, timeStamp))
            etmessage.setText("")

            adapter.insertMessage(Message(message, SEND_ID, timeStamp))
            rvmessages.scrollToPosition(adapter.itemCount - 1)

            botResponse(message)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun botResponse(message: String) {
        val timeStamp = Time.timeStamp()

        GlobalScope.launch {
            //Fake response delay
            delay(1000)

            withContext(Dispatchers.Main) {
                //Gets the response
                val response = BotResponse.basicResponses(message)

                //Adds it to our local list
                messagesList.add(Message(response, RECEIVE_ID, timeStamp))

                //Inserts our message into the adapter
                adapter.insertMessage(Message(response, RECEIVE_ID, timeStamp))

                //Scrolls us to the position of the latest message
                rvmessages.scrollToPosition(adapter.itemCount - 1)

                //Starts Google
                when (response) {
                    OPEN_GOOGLE -> {
                        val site = Intent(Intent.ACTION_VIEW)
                        site.data = Uri.parse("https://www.google.com/")
                        startActivity(site)
                    }
                    OPEN_SEARCH -> {
                        val site = Intent(Intent.ACTION_VIEW)
                        val searchTerm: String = message.substringAfterLast("search")
                        site.data = Uri.parse("https://www.google.com/search?&q=$searchTerm")
                        startActivity(site)
                    }

                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun customBotMessage(message: String) {

        GlobalScope.launch {
            delay(1000)
            withContext(Dispatchers.Main) {
                val timeStamp = Time.timeStamp()
                messagesList.add(Message(message, RECEIVE_ID, timeStamp))
                adapter.insertMessage(Message(message, RECEIVE_ID, timeStamp))

                rvmessages.scrollToPosition(adapter.itemCount - 1)
            }
        }
    }
}
