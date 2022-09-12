package com.example.appfifa

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class fragment3:Fragment(R.layout.fragment3) {
    fun onCreteView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
//        val view = inflater.inflate(R.layout.fragment1,container,false)
        return inflater.inflate(R.layout.fragment1,container,false)
    }
}