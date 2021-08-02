package com.powilliam.anothertodoapp

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.powilliam.anothertodoapp.domain.models.Todo
import com.powilliam.anothertodoapp.domain.repositories.TodoRepository
import kotlinx.coroutines.launch

data class ViewModelState(
    val filterState: Int,
    val todos: List<Todo>
)

sealed class TodosViewModelEvent {
    data class ChangeTodoState(val todo: Todo) : TodosViewModelEvent()
    data class ChangeFilterState(val newFilterState: Int) : TodosViewModelEvent()
}

class TodosViewModel @ViewModelInject constructor(
    private val repository: TodoRepository,
    @Assisted private val savedStateHandle: SavedStateHandle
) : ViewModel() {
    private var _state: MutableLiveData<ViewModelState> = MutableLiveData()
    val state: LiveData<ViewModelState>
        get() = _state

    init {
        refresh()
    }

    fun dispatch(event: TodosViewModelEvent) {
        when (event) {
            is TodosViewModelEvent.ChangeTodoState -> updateTodoState(event.todo)
            is TodosViewModelEvent.ChangeFilterState -> updateFilterState(event.newFilterState)
        }
    }

    private fun updateTodoState(todo: Todo) = viewModelScope.launch {
        val newState = when (todo.state) {
            Todo.STATE_COMPLETE -> Todo.STATE_INCOMPLETE
            else -> Todo.STATE_COMPLETE
        }
        repository.updateState(todo.uuid, newState)
        refresh()
    }

    private fun updateFilterState(newFilterState: Int) = viewModelScope.launch {
        savedStateHandle.set(FILTER_STATE_KEY, newFilterState)
        refresh()
    }

    private fun refresh() = viewModelScope.launch {
        val todos = repository.getAsync().await()
        val savedFilterState = savedStateHandle.get<Int>(FILTER_STATE_KEY) ?: FILTER_ALL
        val todosFilteredBySavedFilterState = when (savedFilterState) {
            FILTER_IMCOMPLETE -> todos.filter { it.state == Todo.STATE_INCOMPLETE }
            FILTER_COMPLETE -> todos.filter { it.state == Todo.STATE_COMPLETE }
            else -> todos
        }
        _state.value =
            ViewModelState(filterState = savedFilterState, todos = todosFilteredBySavedFilterState)
    }

    companion object {
        private const val FILTER_STATE_KEY = "FILTER_STATE"
        const val FILTER_ALL = 0
        const val FILTER_IMCOMPLETE = 1
        const val FILTER_COMPLETE = 2
    }
}