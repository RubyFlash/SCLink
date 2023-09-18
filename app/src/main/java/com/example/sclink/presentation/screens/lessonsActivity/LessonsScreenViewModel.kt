package com.example.sclink.presentation.screens.lessonsActivity

import androidx.lifecycle.*
import com.example.sclink.domain.model.Lesson
import com.example.sclink.domain.repository.LessonsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LessonsScreenViewModel @Inject constructor(
    private val repository: LessonsRepository
) : ViewModel() {

    private var _isLessonEditing = MutableLiveData<Boolean>(false)
    val isLessonEditing: LiveData<Boolean> get() = _isLessonEditing

    fun changeLessonEditingValue(isEditing: Boolean) {
        _isLessonEditing.postValue(isEditing)
    }

    fun getLessonsByDayOfWeek(folderId: Int, dayOfWeek: String): LiveData<List<Lesson>> {
        return repository.getLessonsByDayOfWeek(folderId, dayOfWeek).asLiveData()
    }

    fun insertLessons(lesson: Lesson) = viewModelScope.launch(Dispatchers.IO) {
        repository.insertLesson(lesson)
    }

    fun deleteLessons(lesson: Lesson) = viewModelScope.launch(Dispatchers.IO) {
        repository.deleteLesson(lesson)
    }

    fun updateLessons(lesson: Lesson) = viewModelScope.launch(Dispatchers.IO) {
        repository.updateLesson(lesson)
    }
}

