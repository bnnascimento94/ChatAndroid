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
        fun onFile()
        fun onFoto()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view: View = inflater.inflate(R.layout.bottom_choose_file, container, false)

        val btnArquivo: MaterialButton = view.findViewById(R.id.btnArquivo)
        val btnFoto: MaterialButton = view.findViewById(R.id.btnFoto)


        btnArquivo.setOnClickListener {
            callback?.onFile()
            dismiss()
        }

        btnFoto.setOnClickListener {
            callback?.onFoto()
            dismiss()
        }

        return view
    }
}