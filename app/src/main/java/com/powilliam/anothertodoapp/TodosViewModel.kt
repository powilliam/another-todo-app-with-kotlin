package com.powilliam.anothertodoapp

import androidx.lifecycle.*
import com.powilliam.anothertodoapp.domain.daos.TodoDao
import com.powilliam.anothertodoapp.domain.models.Todo
import kotlinx.coroutines.launch
import java.util.concurrent.Executors

class TodosViewModel(private val todoDao: TodoDao): ViewModel() {
    private var _state: MutableLiveData<ViewModelState> = Transformations.map(todoDao.get()) {
        ViewModelState(
                todos = it,
                incompleteTodos = it.filter { it.state == Todo.STATE_INCOMPLETE },
                completeTodos = it.filter { it.state == Todo.STATE_COMPLETE },
        )
    } as MutableLiveData<ViewModelState>
    val state: LiveData<ViewModelState>
        get() = _state

    data class ViewModelState(
            val filterState: Int = FILTER_ALL,
            val todos: List<Todo>,
            val incompleteTodos: List<Todo>,
            val completeTodos: List<Todo>
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

    fun updateFilterState(newFilterState: Int) = viewModelScope.launch {
        _state.value = _state.value?.copy( filterState = newFilterState )
    }

    companion object {
        private val executor = Executors.newSingleThreadExecutor()
        const val FILTER_ALL = 0
        const val FILTER_IMCOMPLETE = 1
        const val FILTER_COMPLETE = 2
    }
}