package com.example.sclink.presentation.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.example.sclink.domain.repository.WeekTypeRepository
import com.example.sclink.utils.Constants.ALARM_INTENT_STRING_EXTRA
import com.example.sclink.utils.goAsync
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

//@AndroidEntryPoint
//class AlarmReceiver @Inject constructor(
//    private val weekTypeRepository: WeekTypeRepository
//): BroadcastReceiver() {
//
//    override fun onReceive(context: Context?, intent: Intent?) = goAsync {
//        val typeOfWeek = intent?.getStringExtra(ALARM_INTENT_STRING_EXTRA)
//        if (typeOfWeek != null) {
//            weekTypeRepository.setTypeOfWeek(typeOfWeek)
//        }
//    }
//}