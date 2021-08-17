package com.cathay.exammvvmdemo.ui.detail

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.cathay.exammvvmdemo.data.database.AppDataBase
import com.cathay.exammvvmdemo.data.repo.ExamRepo
import com.cathay.exammvvmdemo.ui.result.ResultViewModel

class ResultDetailFactory  (val context: Context): ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        val examRepo = ExamRepo(AppDataBase(context).examInfoDao())
        return ResultDetailViewModel(examRepo) as T
    }
}