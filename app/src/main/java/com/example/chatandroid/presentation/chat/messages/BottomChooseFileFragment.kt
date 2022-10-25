package com.example.chatandroid.presentation.chat.messages

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.chatandroid.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.button.MaterialButton
import java.util.*

class BottomChooseFileFragment(callback: Callback) : BottomSheetDialogFragment(){

    private var callback: Callback? = null
    var cal = Calendar.getInstance()

    init {
        this.callback = callback
    }


    interface Callback {
        fun onCamera()
        fun onGaleria()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view: View = inflater.inflate(R.layout.bottom_choose_file, container, false)

        val btnCamera: MaterialButton = view.findViewById(R.id.btnCamera)
        val btnGaleria: MaterialButton = view.findViewById(R.id.btnGaleria)


        btnCamera.setOnClickListener {
            callback?.onCamera()
            dismiss()
        }

        btnGaleria.setOnClickListener {
            callback?.onGaleria()
            dismiss()
        }

        return view
    }
}