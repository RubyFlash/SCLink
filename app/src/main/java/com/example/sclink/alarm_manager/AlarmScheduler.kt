package com.example.sclink.alarm_manager

interface AlarmScheduler {
    fun schedule(typeOfWeek: String)
    fun cancel()
}