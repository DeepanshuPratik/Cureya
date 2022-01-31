package com.example.cureya.chatbot

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import com.example.cureya.chatbot.Constants.OPEN_GOOGLE
import com.example.cureya.chatbot.Constants.OPEN_SEARCH
import java.sql.Timestamp
import java.text.SimpleDateFormat
import java.util.*

object BotResponse {

    @SuppressLint("SimpleDateFormat")
    @RequiresApi(Build.VERSION_CODES.N)
    fun basicResponses(_message: String): String {

        val random = (0..2).random()
        val message = _message.lowercase(Locale.getDefault())

        return when {

            //Headache
            message.contains("headache") -> {

                " Headache can be reduced by doing following things:\n".plus(
                    "1. Rest in a quiet, dark room\n").plus(
                    "2. Hot or cold compresses to your head or neck\n").plus(
                    "3. Massage and small amounts of caffeine\n").plus(
                    "4. Over-the-counter medications such as ibuprofen (Advil, Motrin IB, others), acetaminophen (Tylenol, others) and aspirin\n").plus(
                    "5.Prescription medications including triptans, such as  sumatriptan (Imitrex) and zolmitriptan (Zomig)\n").plus(
                    "6.Preventive medications such as metoprolol (Lopressor), propranolol (Innopran, Inderal, others), amitriptyline, divalproex (Depakote), topiramate (Qudexy XR, Trokendi XR ,Topamax) or erenumab-aooe (Aimovig)")
            }
            message.contains("ok fine")->{
                "You can start exercising daily to prevent stress and headaches.\n Take Yoga Classes from \n Card Details"
            }

            //Math calculations
            message.contains("solve") -> {
                val equation: String = message.substringAfterLast("solve")
                return try {
                    val answer = SolveMath.solveMath(equation ?: "0")
                    "$answer"

                } catch (e: Exception) {
                    "Sorry, I can't solve that."
                }
            }

            //Hello
            message.contains("hello") -> {
                when (random) {
                    0 -> "Hello there!"
                    1 -> "Sup"
                    2 -> "Bonjour!"
                    else -> "error" }
            }

            //How are you?
            message.contains("how are you") -> {
                when (random) {
                    0 -> "I'm doing fine, thanks!"
                    1 -> "I'm fine. How's you? Is your health going great?"
                    2 -> "Pretty good! How about you? Do you need any medication advice?"
                    else -> "error"
                }
            }

            //What time is it?
            message.contains("time") && message.contains("?")-> {
                val timeStamp = Timestamp(System.currentTimeMillis())
                val sdf = SimpleDateFormat("dd/MM/yyyy HH:mm")
                val date = sdf.format(Date(timeStamp.time))

                date.toString()
            }

            //Open Google
            message.contains("open") && message.contains("google")-> {
                OPEN_GOOGLE
            }

            //Search on the internet
            message.contains("search")-> {
                OPEN_SEARCH
            }

            // Diseases
            //FEVER
            message.contains("fever") && message.contains("a day") || message.contains("1 day")-> {

                    " If you have fever for more than a day, " +
                            "please take Ibugesic Plus two times and wait for a day\n"}

            message.contains("fever") && message.contains("two days") || message.contains("2 days")-> {
                " If you have fever for more than two days, then please consult a doctor \n" }
            message.contains("fever") && message.contains("seven days") || message.contains("week")-> {
                "If you have fever for more than a week and have cold and cough, then please have a COVID test and consult a doctor"

            }

            //When the programme doesn't understand...
            else -> {
                when (random) {
                    0 -> "I don't understand..."
                    1 -> "Try asking me something different"
                    2 -> "Idk"
                    else -> "error"
                }
            }
        }
    }
}