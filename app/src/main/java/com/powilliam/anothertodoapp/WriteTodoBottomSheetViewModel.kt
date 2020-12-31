package com.powilliam.anothertodoapp

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.powilliam.anothertodoapp.domain.daos.TodoDao
import com.powilliam.anothertodoapp.domain.models.Todo
import kotlinx.coroutines.launch
import java.util.concurrent.Executor

class WriteTodoBottomSheetViewModel @ViewModelInject constructor(
    private val todoDao: TodoDao,
    private val singleThreadExecutor: Executor
    ) : ViewModel() {
    fun createTodo(content: String) = viewModelScope.launch {
        singleThreadExecutor.execute {
            val todo = Todo(content = content)
            todoDao.create(todo)
        }
    }

    fun updateTodoContent(uuid: String, content: String) = viewModelScope.launch {
        singleThreadExecutor.execute {
            todoDao.updateContent(uuid, content)
        }
    }
}