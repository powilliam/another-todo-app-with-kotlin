package com.powilliam.anothertodoapp

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.powilliam.anothertodoapp.domain.models.Todo
import com.powilliam.anothertodoapp.domain.repositories.TodoRepository
import kotlinx.coroutines.launch

sealed class WriteTodoBottomSheetEvent {
    data class CreateTodo(val content: String) : WriteTodoBottomSheetEvent()
    data class UpdateTodoContent(val uuid: String, val newContent: String) :
        WriteTodoBottomSheetEvent()

    data class DeleteTodo(val todo: Todo) : WriteTodoBottomSheetEvent()
}

class WriteTodoBottomSheetViewModel @ViewModelInject constructor(
    private val repository: TodoRepository
) : ViewModel() {
    fun dispatch(event: WriteTodoBottomSheetEvent) {
        when (event) {
            is WriteTodoBottomSheetEvent.CreateTodo -> createTodo(event.content)
            is WriteTodoBottomSheetEvent.UpdateTodoContent -> updateTodoContent(
                event.uuid,
                event.newContent
            )
            is WriteTodoBottomSheetEvent.DeleteTodo -> deleteTodo(event.todo)
        }
    }

    private fun createTodo(content: String) = viewModelScope.launch {
        repository.create(content)
    }

    private fun updateTodoContent(uuid: String, content: String) = viewModelScope.launch {
        repository.updateContent(uuid, content)
    }

    private fun deleteTodo(todo: Todo) = viewModelScope.launch {
        repository.delete(todo)
    }
}