package com.cathay.exammvvmdemo

import android.app.Application
import android.util.Log
import androidx.room.Room
import com.cathay.exammvvmdemo.data.database.AppDataBase
import com.cathay.exammvvmdemo.data.database.AppDataBase.Companion.TABLE_NAME
import com.cathay.exammvvmdemo.data.database.ExamRoomDao
import com.google.gson.Gson


class App :Application(){
    override fun onCreate() {
        super.onCreate()

    }
}