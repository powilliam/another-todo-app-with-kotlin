package com.powilliam.anothertodoapp.domain.repositories

import androidx.lifecycle.LiveData
import com.powilliam.anothertodoapp.domain.daos.TodoDao
import com.powilliam.anothertodoapp.domain.models.Todo
import kotlinx.coroutines.*
import javax.inject.Inject

class TodoRepository @Inject constructor(
    private val todoDao: TodoDao,
) {
    suspend fun getAsync(): Deferred<List<Todo>> = withContext(Dispatchers.IO) {
        async {
            todoDao.get()
        }
    }

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