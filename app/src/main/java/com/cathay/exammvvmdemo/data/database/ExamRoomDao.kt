package com.cathay.exammvvmdemo.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
@Entity(tableName = AppDataBase.TABLE_NAME)
class ExamRoomDao{
    @PrimaryKey(autoGenerate = true)
    var id:Int = 0
    var topic: String = ""
    var userAns: String = ""
    var ans: String = ""
    var options: String = ""
    var isCorrect: Boolean = true

}














