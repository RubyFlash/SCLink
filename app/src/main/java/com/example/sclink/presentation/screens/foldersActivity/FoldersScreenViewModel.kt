package com.example.sclink.presentation.screens.foldersActivity

import androidx.lifecycle.*
import com.example.sclink.domain.model.Folder
import com.example.sclink.domain.repository.FoldersRepository
import com.example.sclink.domain.repository.WeekTypeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoldersScreenViewModel @Inject constructor(
    private val foldersRepository: FoldersRepository,
    private val weekTypeRepository: WeekTypeRepository
) : ViewModel() {

    private val _isFolderEditing = MutableLiveData(false)
    val isFolderEditing: LiveData<Boolean> get() = _isFolderEditing

    private val _isFabClicked = MutableLiveData(false)
    val isFabClicked: LiveData<Boolean> get() = _isFabClicked

    private val _typeOfWeek = MutableLiveData("")
    val typeOfWeek: LiveData<String> get() = _typeOfWeek

    fun gettTypeOfWeek() = weekTypeRepository.getTypeOfWeek().asLiveData()

    fun setTypeOfWeek(typeOfWeek: String) = viewModelScope.launch(Dispatchers.IO) {
        weekTypeRepository.setTypeOfWeek(typeOfWeek = typeOfWeek)
    }

    fun updateFabClickedValue(clicked: Boolean) {
        _isFabClicked.postValue(clicked)
    }

    fun updateFolderEditingValue(isEditing: Boolean) {
        _isFolderEditing.postValue(isEditing)
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

