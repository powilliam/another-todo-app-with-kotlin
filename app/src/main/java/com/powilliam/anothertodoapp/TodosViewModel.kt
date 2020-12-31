package com.powilliam.anothertodoapp

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.powilliam.anothertodoapp.domain.daos.TodoDao
import com.powilliam.anothertodoapp.domain.models.Todo
import kotlinx.coroutines.launch
import java.util.concurrent.Executor

class TodosViewModel @ViewModelInject constructor(
    private val todoDao: TodoDao,
    private val singleThreadExecutor: Executor,
    @Assisted private val savedStateHandle: SavedStateHandle
    ): ViewModel() {
    private var _state: MutableLiveData<ViewModelState> = Transformations.map(todoDao.get()) {
        ViewModelState(
                filterState = savedStateHandle.get<Int>(FILTER_STATE_KEY) ?: FILTER_ALL,
                todos = it,
                incompleteTodos = it.filter { it.state == Todo.STATE_INCOMPLETE },
                completeTodos = it.filter { it.state == Todo.STATE_COMPLETE },
        )
    } as MutableLiveData<ViewModelState>
    val state: LiveData<ViewModelState>
        get() = _state

    data class ViewModelState(
            val filterState: Int,
            val todos: List<Todo>,
            val incompleteTodos: List<Todo>,
            val completeTodos: List<Todo>
    )

    fun updateTodoState(todo: Todo) = viewModelScope.launch {
        singleThreadExecutor.execute {
            val newState = when (todo.state) {
                Todo.STATE_COMPLETE -> Todo.STATE_INCOMPLETE
                else -> Todo.STATE_COMPLETE
            }
            todoDao.updateState(todo.uuid, newState)
        }
    }

    fun updateFilterState(newFilterState: Int) = viewModelScope.launch {
        _state.value = _state.value?.copy( filterState = newFilterState )
        savedStateHandle.set(FILTER_STATE_KEY, newFilterState)
    }

    companion object {
        private const val FILTER_STATE_KEY = "FILTER_STATE"
        const val FILTER_ALL = 0
        const val FILTER_IMCOMPLETE = 1
        const val FILTER_COMPLETE = 2
    }
}