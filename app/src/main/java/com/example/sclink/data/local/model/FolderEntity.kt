package com.example.sclink.data.local.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "folders_table")
data class FolderEntity(
    @PrimaryKey(autoGenerate = true)
    val folderId: Int = 0,
    val folderName: String
)

