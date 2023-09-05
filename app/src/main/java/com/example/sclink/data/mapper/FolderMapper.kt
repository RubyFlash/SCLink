package com.example.sclink.data.mapper

import com.example.sclink.data.local.model.FolderEntity
import com.example.sclink.domain.model.Folder

fun FolderEntity.toFolders(): Folder {
    return Folder(
        folderId = folderId,
        folderName = folderName
    )
}

fun Folder.toFoldersEntity(): FolderEntity {
    return FolderEntity(
        folderId = folderId,
        folderName = folderName
    )
}

