package com.example.sclink.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.sclink.data.local.dao.FoldersDao
import com.example.sclink.data.local.dao.LessonsDao
import com.example.sclink.data.model.FolderEntity
import com.example.sclink.data.model.LessonEntity

@Database(entities = [FolderEntity::class, LessonEntity::class], version = 5)
abstract class Database: RoomDatabase() {
    abstract val foldersDao: FoldersDao
    abstract val lessonsDao: LessonsDao
}