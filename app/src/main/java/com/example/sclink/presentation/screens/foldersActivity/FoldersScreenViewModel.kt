package com.example.sclink.presentation.screens.foldersActivity

import androidx.lifecycle.*
import com.example.sclink.alarm_manager.AlarmScheduler
import com.example.sclink.domain.model.Folder
import com.example.sclink.domain.repository.DataStoreOperations
import com.example.sclink.domain.repository.FoldersRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FoldersScreenViewModel @Inject constructor(
    private val foldersRepository: FoldersRepository,
    private val dataStoreOperations: DataStoreOperations,
    private val alarmScheduler: AlarmScheduler
) : ViewModel() {

    private val _isFolderEditing = MutableLiveData(false)
    val isFolderEditing: LiveData<Boolean> get() = _isFolderEditing

    private val _isFabClicked = MutableLiveData(false)
    val isFabClicked: LiveData<Boolean> get() = _isFabClicked

    private val _isNotificationBtnClicked = MutableStateFlow(false)
    val isNotificationBtnClicked: StateFlow<Boolean> get() = _isNotificationBtnClicked

    private val _typeOfWeek = MutableStateFlow("")
    val typeOfWeek: StateFlow<String> get() = _typeOfWeek

    init {
        viewModelScope.launch {
            dataStoreOperations.getTypeOfWeek().collect { typeOfWeek ->
                _typeOfWeek.value = typeOfWeek
            }
        }
    }

    fun setAlarmScheduler(typeOfWeek: String) {
        alarmScheduler.schedule(typeOfWeek = typeOfWeek)
    }

    fun cancelAlarmScheduler() {
        alarmScheduler.cancel()
    }

    fun setTypeOfWeek(typeOfWeek: String) = viewModelScope.launch(Dispatchers.IO) {
        dataStoreOperations.setTypeOfWeek(typeOfWeek = typeOfWeek)
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

