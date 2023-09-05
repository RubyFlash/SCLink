package com.example.sclink.domain.repository

import com.example.sclink.domain.model.Lesson
import kotlinx.coroutines.flow.Flow

interface LessonsRepository {

    fun getLessonsByDayOfWeek(folderId: Int, dayOfWeek: String): Flow<List<Lesson>>

    suspend fun insertLesson(lesson: Lesson)

    suspend fun deleteLesson(lesson: Lesson)

    suspend fun updateLesson(lesson: Lesson)
}