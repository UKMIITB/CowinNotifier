package com.example.cowinnotifier.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.cowinnotifier.R
import kotlinx.android.synthetic.main.fragment_pincode_search.*


class PincodeSearchFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pincode_search, container, false)
    }

    fun getPincode(): String {
        return edit_text_pincode.text.toString()
    }
}