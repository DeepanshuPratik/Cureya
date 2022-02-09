package com.example.cureya.chatbot

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.cureya.R
import com.google.api.gax.core.FixedCredentialsProvider
import com.google.auth.oauth2.GoogleCredentials
import com.google.auth.oauth2.ServiceAccountCredentials
import com.google.cloud.dialogflow.v2.*
import kotlinx.coroutines.*
import kotlinx.coroutines.Dispatchers.Default
import java.util.*
import kotlin.collections.ArrayList
import kotlinx.coroutines.DelicateCoroutinesApi as CoroutinesDelicateCoroutinesApi

class ChatbotFragment : Fragment() {

    private var messageList: ArrayList<Message> = ArrayList()
    private val botList = listOf("Deepanshu", "Divyansh", "Ronnie", "Anmol")
    private lateinit var rvmessages : RecyclerView
    private lateinit var etmessage : EditText

    //dialogFlow
    private var sessionsClient: SessionsClient? = null
    private var sessionName: SessionName? = null
    private val uuid = UUID.randomUUID().toString()
    private val TAG = "mainactivity"
    private lateinit var chatAdapter: ChatAdapter

    @SuppressLint("NotifyDataSetChanged")
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //initialize bot config
        setUpBot()
        return inflater.inflate(R.layout.fragment_chatbot, container, false)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        rvmessages = view.findViewById<RecyclerView>(R.id.rv_messages)
        etmessage = view.findViewById<EditText>(R.id.et_message)
        chatAdapter = ChatAdapter(this,messageList)
        rvmessages.adapter = chatAdapter
        rvmessages.layoutManager = LinearLayoutManager(activity)
        view.findViewById<ImageView>(R.id.send_button)?.setOnClickListener {

            sendMessage()
        }
    }
    override fun onStart() {
        super.onStart()
        //In case there are messages, scroll to bottom when re-opening app
        GlobalScope.launch {
            delay(100)
            withContext(Dispatchers.Main) {
                rvmessages.scrollToPosition(chatAdapter.itemCount - 1)
            }
        }
    }
    @CoroutinesDelicateCoroutinesApi
    @RequiresApi(Build.VERSION_CODES.N)
    private fun sendMessage(){
        val message: String = etmessage.text.toString()
        if (message.isNotEmpty()) {
            addMessageToList(message, false)
            if(messageList.isNotEmpty()) {
                requireView().findViewById<TextView>(R.id.welcome).visibility = View.GONE
                requireView().findViewById<ImageView>(R.id.chatbot).visibility = View.GONE
                requireView().findViewById<TextView>(R.id.Intro).visibility = View.GONE
            }
            sendMessageToBot(message)
        } else {
            Toast.makeText(activity, "Please enter text!", Toast.LENGTH_SHORT).show()
        }
    }


    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("NotifyDataSetChanged")
    private fun addMessageToList(message: String, isReceived: Boolean) {
        val timeStamp = Time.timeStamp()
        messageList.add(Message(message, isReceived,timeStamp))
        etmessage.setText("")
        chatAdapter.notifyDataSetChanged()
        rvmessages.layoutManager?.scrollToPosition(messageList.size - 1)
    }

    private fun setUpBot() {
        try {
            val stream = this.resources.openRawResource(R.raw.credentials)
            val credentials: GoogleCredentials = GoogleCredentials.fromStream(stream)
                .createScoped("https://www.googleapis.com/auth/cloud-platform")
            val projectId: String = (credentials as ServiceAccountCredentials).projectId
            val settingsBuilder: SessionsSettings.Builder = SessionsSettings.newBuilder()
            val sessionsSettings: SessionsSettings = settingsBuilder.setCredentialsProvider(
                FixedCredentialsProvider.create(credentials)
            ).build()
            sessionsClient = SessionsClient.create(sessionsSettings)
            sessionName = SessionName.of(projectId, uuid)
            Log.d(TAG, "projectId : $projectId")
        } catch (e: Exception) {
            Log.d(TAG, "setUpBot: " + e.message)
        }
    }

    @CoroutinesDelicateCoroutinesApi
    @RequiresApi(Build.VERSION_CODES.N)
    private fun sendMessageToBot(message: String) {
        val input = QueryInput.newBuilder()
            .setText(TextInput.newBuilder().setText(message).setLanguageCode("en-US")).build()
        GlobalScope.launch {
            sendMessageInBg(input)
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private suspend fun sendMessageInBg(
        queryInput: QueryInput
    ) {
        withContext(Default) {
            try {
                val detectIntentRequest = DetectIntentRequest.newBuilder()
                    .setSession(sessionName.toString())
                    .setQueryInput(queryInput)
                    .build()
                val result = sessionsClient?.detectIntent(detectIntentRequest)
                if (result != null) activity?.runOnUiThread {
                    updateUI(result)
                }
            } catch (e: java.lang.Exception) {
                Log.d(TAG, "doInBackground: " + e.message)
                e.printStackTrace()
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun updateUI(response: DetectIntentResponse) {
        val botReply: String = response.queryResult.fulfillmentText
        if (botReply.isNotEmpty()) {
            addMessageToList(botReply, true)
        } else {
            Toast.makeText(activity, "something went wrong", Toast.LENGTH_SHORT).show()
        }
    }
}