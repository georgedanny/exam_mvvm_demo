package com.cathay.exammvvmdemo.ui.detail

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cathay.exammvvmdemo.data.entity.ExamEntity
import com.cathay.exammvvmdemo.data.repo.ExamRepo
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ResultDetailViewModel(private val repo: ExamRepo): ViewModel() {
    private val _liveExamInfo = MutableLiveData<ExamEntity>()

    val liveExamInfo: LiveData<ExamEntity> = _liveExamInfo

    private val _previousStateLive = MutableLiveData<Int>()

    val previousStateLive: LiveData<Int> = _previousStateLive

    private val _nextStateLive = MutableLiveData<Int>()

    val nextStateLive: LiveData<Int> = _nextStateLive

    //get Exam Data
    fun requestExam(position:Int){
        viewModelScope.launch{
            if(position >= 0){
                repo.fetchExams()
                    .catch {
                        Log.e("Ryan", Log.getStackTraceString(it))
                    }
                    .collect {
                        val exam = it[position]
                        _liveExamInfo.postValue(exam)
                        updateButtonState(position,it.size)
                    }
            }
        }
    }

    //update checkbox check state
    private fun updateButtonState(position: Int, size:Int){
        if (position == 0){
            _previousStateLive.postValue(View.GONE)
        }else{
            _previousStateLive.postValue(View.VISIBLE)
        }

        if (position == size-1){
            _nextStateLive.postValue(View.GONE)
        }else{
            _nextStateLive.postValue(View.VISIBLE)
        }
    }
}