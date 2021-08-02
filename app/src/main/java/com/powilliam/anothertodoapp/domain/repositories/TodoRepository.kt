package com.powilliam.anothertodoapp.domain.repositories

import androidx.lifecycle.LiveData
import com.powilliam.anothertodoapp.domain.daos.TodoDao
import com.powilliam.anothertodoapp.domain.models.Todo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TodoRepository @Inject constructor(
    private val todoDao: TodoDao,
) {
    fun get(): LiveData<List<Todo>> = todoDao.get()

    suspend fun create(content: String) = withContext(Dispatchers.IO) {
        launch {
            val todo = Todo(content = content)
            todoDao.create(todo)
        }
    }

    suspend fun updateState(uuid: String, newState: Int) = withContext(Dispatchers.IO) {
        launch {
            todoDao.updateState(uuid, newState)
        }
    }

    suspend fun updateContent(uuid: String, newContent: String) = withContext(Dispatchers.IO) {
        launch {
            todoDao.updateContent(uuid, newContent)
        }
    }

    suspend fun delete(todo: Todo) = withContext(Dispatchers.IO) {
        launch {
            todoDao.delete(todo)
        }
    }
}