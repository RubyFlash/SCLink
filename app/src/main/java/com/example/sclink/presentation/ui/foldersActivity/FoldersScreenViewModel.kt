package com.example.sclink.presentation.ui.foldersActivity

import androidx.lifecycle.*
import com.example.sclink.domain.model.Folder
import com.example.sclink.domain.repository.FoldersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoldersScreenViewModel @Inject constructor(
    private val foldersRepository: FoldersRepository
) : ViewModel() {

    private var _isFolderEditing = MutableLiveData<Boolean>(false)
    val isFolderEditing: LiveData<Boolean> get() = _isFolderEditing

    fun changeFolderEditingValue() {
        _isFolderEditing.value?.let {
            _isFolderEditing.value = !it
        }
    }

    fun getAllFolders() = foldersRepository.getFolders().asLiveData()

    fun insertFolder(folder: Folder) = viewModelScope.launch(Dispatchers.IO) {
        foldersRepository.insertFolder(folder)
    }

    fun deleteFolder(folder: Folder) = viewModelScope.launch(Dispatchers.IO) {
        foldersRepository.deleteFolder(folder)
    }

    fun updateFolder(folder: Folder) = viewModelScope.launch(Dispatchers.IO) {
        foldersRepository.updateFolder(folder)
    }
}

