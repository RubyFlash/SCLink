package com.example.sclink.domain.model

data class Lesson(
    val lessonId: Int = 0,
    val folderId: Int,
    val dayOfWeek: String,
    val lessonName: String,
    val lessonLink: String
)