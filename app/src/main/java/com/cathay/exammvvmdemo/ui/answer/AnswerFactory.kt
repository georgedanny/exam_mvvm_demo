package com.cathay.exammvvmdemo.ui.answer

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cathay.exammvvmdemo.data.database.AppDataBase
import com.cathay.exammvvmdemo.data.repo.ExamRepo
import com.cathay.exammvvmdemo.ui.home.HomeViewModel

class AnswerFactory(val context: Context) :
    ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val examRepo = ExamRepo(AppDataBase(context).examInfoDao())
        return AnswerViewModel(examRepo) as T
    }
}