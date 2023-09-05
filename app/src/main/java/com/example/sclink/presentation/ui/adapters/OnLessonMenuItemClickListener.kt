package com.example.sclink.presentation.ui.adapters

interface OnLessonMenuItemClickListener {
    fun onLessonDeleteButtonClick(position: Int)
    fun onLessonEditButtonClick(position: Int)
    fun onLessonSendButtonClick(position: Int)
}