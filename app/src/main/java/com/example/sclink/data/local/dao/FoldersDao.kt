package com.example.sclink.data.local.dao

import androidx.room.*
import com.example.sclink.data.model.FolderEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface FoldersDao {
    @Query("SELECT * FROM folders_table ORDER BY folderId ASC")
    fun getAllFolders(): Flow<List<FolderEntity>>

    @Insert
    suspend fun addFolder(folder: FolderEntity)

    @Update
    suspend fun updateFolder(folder: FolderEntity)

    @Delete
    suspend fun deleteFolder(folder: FolderEntity)
}

