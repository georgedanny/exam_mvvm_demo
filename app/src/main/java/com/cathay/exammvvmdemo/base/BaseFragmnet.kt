package com.cathay.exammvvmdemo.base

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment

abstract class BaseFragmnet:Fragment(){

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        listen()
        Observe()
    }

    abstract fun Observe()



    abstract fun initView()



    abstract fun listen()



    protected fun nav(): NavController {
        return NavHostFragment.findNavController(this)
    }



}