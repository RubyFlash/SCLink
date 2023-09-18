package com.example.sclink.data.local.dao

import androidx.room.*
import com.example.sclink.data.model.LessonEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LessonsDao{
    @Query("SELECT * FROM lessons_table WHERE folderId = :folderId AND dayOfWeek = :dayOfWeek")
    fun getLessonsByFolder(folderId: Int, dayOfWeek: String): Flow<List<LessonEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addLesson(lesson: LessonEntity)

    @Update
    suspend fun updateLesson(lesson: LessonEntity)

    @Delete
    suspend fun deleteLesson(lesson: LessonEntity)
}