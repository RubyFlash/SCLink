package com.example.sclink.data.repository

import com.example.sclink.data.local.dao.LessonsDao
import com.example.sclink.data.mapper.toLessons
import com.example.sclink.data.mapper.toLessonsEntity
import com.example.sclink.domain.model.Lesson
import com.example.sclink.domain.repository.LessonsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LessonsRepositoryImpl @Inject constructor(
    private val lessonsDao: LessonsDao
): LessonsRepository {

    override fun getLessonsByDayOfWeek(folderId: Int, dayOfWeek: String): Flow<List<Lesson>> {
        return lessonsDao.getLessonsByFolder(folderId, dayOfWeek).map {it.toList().map { it.toLessons() }}
    }

    override suspend fun insertLesson(lesson: Lesson) {
        lessonsDao.addLesson(lesson.toLessonsEntity())
    }

    override suspend fun deleteLesson(lesson: Lesson) {
        lessonsDao.deleteLesson(lesson.toLessonsEntity())
    }

    override suspend fun updateLesson(lesson: Lesson) {
        lessonsDao.updateLesson(lesson.toLessonsEntity())
    }
}