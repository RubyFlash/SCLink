package com.example.sclink.data.repository

import com.example.sclink.data.local.dao.FoldersDao
import com.example.sclink.data.mapper.toFolders
import com.example.sclink.data.mapper.toFoldersEntity
import com.example.sclink.domain.model.Folder
import com.example.sclink.domain.repository.FoldersRepository
import kotlinx.coroutines.flow.*
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FoldersRepositoryImpl @Inject constructor(
    private val foldersDao: FoldersDao
) : FoldersRepository {

    override fun getFolders(): Flow<List<Folder>> {
        return foldersDao.getAllFolders().map { it.toList().map { it.toFolders() } }
    }

    override suspend fun insertFolder(folder: Folder) {
        foldersDao.addFolder(folder = folder.toFoldersEntity())
    }

    override suspend fun deleteFolder(folder: Folder) {
        foldersDao.deleteFolder(folder = folder.toFoldersEntity())
    }

    override suspend fun updateFolder(folder: Folder) {
        foldersDao.updateFolder(folder = folder.toFoldersEntity())
    }
}
