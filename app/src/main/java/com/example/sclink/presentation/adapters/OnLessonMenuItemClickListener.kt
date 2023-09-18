package com.example.sclink.presentation.adapters

interface OnLessonMenuItemClickListener {
    fun onLessonDeleteButtonClick(position: Int)
    fun onLessonEditButtonClick(position: Int)
    fun onLessonSendButtonClick(position: Int)
}