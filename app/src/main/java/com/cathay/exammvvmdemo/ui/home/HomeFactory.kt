package com.cathay.exammvvmdemo.ui.home

import android.app.Application
import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cathay.exammvvmdemo.data.database.AppDataBase
import com.cathay.exammvvmdemo.data.repo.ExamRepo


    class HomeFactory(val context: Context) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            val examRepo = ExamRepo(AppDataBase(context).examInfoDao())
            return HomeViewModel(examRepo) as T
        }
    }
