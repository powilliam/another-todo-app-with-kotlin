package com.powilliam.anothertodoapp

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.powilliam.anothertodoapp.domain.models.Todo
import com.powilliam.anothertodoapp.domain.repositories.TodoRepository
import kotlinx.coroutines.launch

class WriteTodoBottomSheetViewModel @ViewModelInject constructor(
    private val repository: TodoRepository
) : ViewModel() {
    fun createTodo(content: String) = viewModelScope.launch {
        repository.create(content)
    }

    fun updateTodoContent(uuid: String, content: String) = viewModelScope.launch {
        repository.updateContent(uuid, content)
    }

    fun deleteTodo(todo: Todo) = viewModelScope.launch {
        repository.delete(todo)
    }
}