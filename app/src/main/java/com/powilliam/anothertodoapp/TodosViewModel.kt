package com.powilliam.anothertodoapp

import androidx.lifecycle.*
import com.powilliam.anothertodoapp.domain.daos.TodoDao
import com.powilliam.anothertodoapp.domain.models.Todo
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

class TodosViewModel(private val todoDao: TodoDao): ViewModel() {
    private var _state: MutableLiveData<ViewModelState> = Transformations.map(todoDao.get()) {
        ViewModelState(todos = it)
    } as MutableLiveData<ViewModelState>
    val state: LiveData<ViewModelState>
        get() = _state

    data class ViewModelState(
        val todos: List<Todo>
    )

    fun updateTodoState(todo: Todo) = viewModelScope.launch {
        executor.execute {
            val newState = when (todo.state) {
                Todo.STATE_COMPLETE -> Todo.STATE_INCOMPLETE
                else -> Todo.STATE_COMPLETE
            }
            todoDao.updateState(todo.uuid, newState)
        }
    }

    companion object {
        private val executor = Executors.newSingleThreadExecutor()
    }
}