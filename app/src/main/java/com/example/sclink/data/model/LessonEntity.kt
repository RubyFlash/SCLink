package com.example.sclink.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "lessons_table")
data class LessonEntity(
    @PrimaryKey(autoGenerate = true)
    val lessonId: Int = 0,
    val folderId: Int,
    val dayOfWeek: String,
    val lessonName: String,
    val lessonLink: String
)