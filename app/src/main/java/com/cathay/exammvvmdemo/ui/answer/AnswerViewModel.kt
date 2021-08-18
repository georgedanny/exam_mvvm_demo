package com.cathay.exammvvmdemo.ui.answer

import android.util.Log
import android.view.View
import android.widget.CheckBox
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cathay.exammvvmdemo.data.ClickType
import com.cathay.exammvvmdemo.data.entity.ExamEntity
import com.cathay.exammvvmdemo.data.repo.ExamRepo
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class AnswerViewModel(val repo: ExamRepo) : ViewModel() {
    private val _liveExamInfo = MutableLiveData<ExamEntity>()

    val liveExamInfo: LiveData<ExamEntity> = _liveExamInfo

    private val _previousStateLive = MutableLiveData<Int>()

    val previousStateLive: LiveData<Int> = _previousStateLive

    private val _nextStateLive = MutableLiveData<Int>()

    val nextStateLive: LiveData<Int> = _nextStateLive

    private val _saveLive = MutableLiveData<ClickType>()

    val saveLive: LiveData<ClickType> = _saveLive

    private var checkDatas = mutableListOf<Boolean>()

    //get Exam Data
    fun requestExam(position: Int) {
        viewModelScope.launch {
            if (position >= 0) {
                repo.fetchExams()
                    .catch {
                        Log.e("Ryan", Log.getStackTraceString(it))
                    }
                    .collect {
                        val exam = it[position]
                        _liveExamInfo.postValue(exam)
                        updateButtonState(position, it.size)
                    }
            }
        }
    }

    //Save Answer to DB
    fun requestSaveAnswer(clickType: ClickType) {
        viewModelScope.launch {
            _liveExamInfo.value?.let { entity ->
                val ans = StringBuilder()
                val array = mutableListOf("1", "2", "3", "4")
                checkDatas.mapIndexed { index, b ->
                    if (b) {
                        ans.append(array[index])
                    }
                    if (index < array.size - 1) {
                        ans.append(",")
                    }
                }
                entity.userAns = ans.toString()
                repo.saveAnswer(entity).collect {
                    Log.d("Ryan", "save success")
                    _saveLive.postValue(clickType)
                }
            }
        }
    }

    fun initCheckList(list:MutableList<Boolean>){
        checkDatas = list
    }

    //update checkbox check state
    fun updateCheckState(position: Int) {
        checkDatas.apply {
            set(position, !checkDatas[position])
        }
    }

    //update previous next button state
    private fun updateButtonState(position: Int, size: Int) {
        if (position == 0) {
            _previousStateLive.postValue(View.GONE)
        } else {
            _previousStateLive.postValue(View.VISIBLE)
        }

        if (position == size - 1) {
            _nextStateLive.postValue(View.GONE)
        } else {
            _nextStateLive.postValue(View.VISIBLE)
        }
    }
}