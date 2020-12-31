package com.powilliam.anothertodoapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.powilliam.anothertodoapp.domain.daos.TodoDao
import com.powilliam.anothertodoapp.domain.models.Todo
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

class WriteTodoBottomSheetViewModel(private val todoDao: TodoDao) : ViewModel() {
    fun createTodo(content: String) = viewModelScope.launch {
        executor.execute {
            val todo = Todo(content = content)
            todoDao.create(todo)
        }
    }

    fun updateTodoContent(uuid: String, content: String) = viewModelScope.launch {
        executor.execute {
            todoDao.updateContent(uuid, content)
        }
    }

    companion object {
        private val executor = Executors.newSingleThreadExecutor()
    }
}