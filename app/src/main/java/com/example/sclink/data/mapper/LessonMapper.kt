package com.example.sclink.data.mapper

import com.example.sclink.data.local.model.LessonEntity
import com.example.sclink.domain.model.Lesson

fun LessonEntity.toLessons(): Lesson {
    return Lesson(
        lessonId = lessonId,
        folderId = folderId,
        dayOfWeek = dayOfWeek,
        lessonName = lessonName,
        lessonLink = lessonLink
    )
}

fun Lesson.toLessonsEntity(): LessonEntity {
    return LessonEntity(
        lessonId = lessonId,
        folderId = folderId,
        dayOfWeek = dayOfWeek,
        lessonName = lessonName,
        lessonLink = lessonLink
    )
}