package com.cathay.exammvvmdemo.data.repo

import com.cathay.exammvvmdemo.data.database.ExamRoomDao
import com.cathay.exammvvmdemo.data.database.RoomDao
import com.cathay.exammvvmdemo.data.entity.ExamEntity
import com.cathay.exammvvmdemo.utils.DataUtil
import com.google.gson.Gson
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class ExamRepo( private val roomDao: RoomDao) {

    fun fetchExams(): Flow<MutableList<ExamEntity>> {
        return flow<MutableList<ExamEntity>> {
            val exams = roomDao.getAll()
            if (exams != null) {
                if (exams.isNotEmpty()) {
                    val data = trans(exams)
                    emit(data)
                } else {
                    val data = DataUtil.createData()
                    roomDao.insertAll(data)
                    val initData = trans(data)
                    emit(initData)
                }
            }
        }.flowOn(Dispatchers.IO)
    }

    fun resetDefaultTable():Flow<Unit>{
        return flow<Unit> {
            roomDao.clearTable()
            emit(Unit)
        }.flowOn(Dispatchers.IO)
    }

    fun saveAnswer(entity: ExamEntity):Flow<Unit>{
        return flow<Unit> {
            Gson().apply {
                val dao =  fromJson(toJson(entity),ExamRoomDao::class.java)
                emit(roomDao.update(dao))
            }


        }.flowOn(Dispatchers.IO)
    }


    private fun trans(exams: List<ExamRoomDao>): MutableList<ExamEntity> {
        val examEntityList = mutableListOf<ExamEntity>()
        exams.map {
            val examEntity = ExamEntity()
            examEntity.id = it.id
            examEntity.topic = it.topic
            examEntity.ans = it.ans
            examEntity.userAns = it.userAns
            examEntity.options = it.options
            examEntityList.add(examEntity)
        }
        return examEntityList
    }




}




