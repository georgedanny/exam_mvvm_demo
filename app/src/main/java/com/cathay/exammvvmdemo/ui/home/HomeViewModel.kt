package com.cathay.exammvvmdemo.ui.home


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.cathay.exammvvmdemo.data.entity.ExamEntity
import com.cathay.exammvvmdemo.data.repo.ExamRepo
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class HomeViewModel(private val repo: ExamRepo) : ViewModel() {
    private val _liveExamInfo = MutableLiveData<List<ExamEntity>>()

    val liveExamInfo: LiveData<List<ExamEntity>> = _liveExamInfo

    fun requestExams(){
        viewModelScope.launch{
            repo.fetchExams()
                .catch {
                    Log.e("Ryan",Log.getStackTraceString(it))
                }
                .collect {
                    _liveExamInfo.postValue(it)
                 }
        }
    }
}