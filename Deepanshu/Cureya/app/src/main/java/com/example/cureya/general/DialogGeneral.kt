package com.example.cureya.general

import android.app.AlertDialog
import android.app.Dialog
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