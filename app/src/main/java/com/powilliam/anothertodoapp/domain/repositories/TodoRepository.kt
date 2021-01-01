package com.powilliam.anothertodoapp.domain.repositories

import androidx.lifecycle.LiveData
import com.powilliam.anothertodoapp.domain.daos.TodoDao
import com.powilliam.anothertodoapp.domain.models.Todo
import java.util.concurrent.Executor
import javax.inject.Inject

class TodoRepository @Inject constructor(
        private val todoDao: TodoDao,
        private val executor: Executor
) {
    fun get(): LiveData<List<Todo>> = todoDao.get()

    fun create(content: String) = executor.execute {
        val todo = Todo(content = content)
        todoDao.create(todo)
    }

    fun updateState(uuid: String, newState: Int) = executor.execute {
        todoDao.updateState(uuid, newState)
    }

    fun updateContent(uuid: String, newContent: String) = executor.execute {
        todoDao.updateContent(uuid, newContent)
    }

    fun delete(todo: Todo) = executor.execute {
        todoDao.delete(todo)
    }
}