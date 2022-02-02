package com.example.cureya.general

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.cureya.R
import com.example.cureya.databinding.DialogGeneralBinding

class DialogGeneral : DialogFragment() {

    private lateinit var listener: GeneralDialogListener

    interface  GeneralDialogListener {
        fun onPositiveClick()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            listener = parentFragment as GeneralDialogListener
        } catch (e: ClassCastException) {
            throw  ClassCastException("context must implement listener")
        }
    }

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    /*override fun onAttach(context: Context) {
        super.onAttach(context)
        // Verify that the host activity implements the callback interface
        try {
            // Instantiate the NoticeDialogListener so we can send events to the host
            listener = context as GeneralDialogListener
        } catch (e: ClassCastException) {
            // The activity doesn't implement the interface, throw exception
            throw ClassCastException("$context must implement NoticeDialogListener")
        }
    }*/

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return activity?.let {
            val builder = AlertDialog.Builder(it)
            val inflater = requireActivity().layoutInflater
            val view = inflater.inflate(R.layout.dialog_general, null)
            val binding = DialogGeneralBinding.bind(view)

            binding.dialogText.text = getString(R.string.dialog_sign_out_warning_text)

            builder.setView(view)
                .setPositiveButton(R.string.ok) { _, _ -> listener.onPositiveClick() }
                .setNeutralButton(R.string.no) { _, _ -> }

            builder.create()
        } ?: throw IllegalStateException("Activity can't be null")
    }
}