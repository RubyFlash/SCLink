package com.example.sclink.domain.repository

import com.example.sclink.domain.model.Folder
import kotlinx.coroutines.flow.Flow

interface FoldersRepository {

    fun getFolders(): Flow<List<Folder>>

    suspend fun insertFolder(folder: Folder)

    suspend fun deleteFolder(folder: Folder)

    suspend fun updateFolder(folder: Folder)

}

