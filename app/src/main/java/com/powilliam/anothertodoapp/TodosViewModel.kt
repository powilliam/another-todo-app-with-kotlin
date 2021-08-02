package com.powilliam.anothertodoapp

import androidx.annotation.IdRes
import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import com.powilliam.anothertodoapp.domain.models.Todo
import com.powilliam.anothertodoapp.domain.repositories.TodoRepository
import kotlinx.coroutines.launch
import java.io.Serializable

sealed class FilterState(val value: Int, @IdRes val res: Int) : Serializable {
    object All : FilterState(value = TodosViewModel.FILTER_ALL, res = R.id.filter_all)

    object Incomplete : FilterState(
        value = TodosViewModel.FILTER_IMCOMPLETE,
        res = R.id.filter_incomplete
    )

    object Complete : FilterState(
        value = TodosViewModel.FILTER_COMPLETE,
        res = R.id.filter_complete
    )
}

data class ViewModelState(
    val filterState: FilterState,
    val todos: List<Todo>
)

sealed class TodosViewModelEvent {
    data class ChangeTodoState(val todo: Todo) : TodosViewModelEvent()
    data class ChangeFilterState(val newFilterState: FilterState) : TodosViewModelEvent()
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

    private fun updateFilterState(newFilterState: FilterState) = viewModelScope.launch {
        savedStateHandle.set(FILTER_STATE_KEY, newFilterState)
        refresh()
    }

    private fun refresh() = viewModelScope.launch {
        val todos = repository.getAsync().await()
        val savedFilterState =
            savedStateHandle.get<FilterState>(FILTER_STATE_KEY) ?: FilterState.All
        val todosFilteredBySavedFilterState = when (savedFilterState) {
            is FilterState.Incomplete -> todos.filter { it.state == Todo.STATE_INCOMPLETE }
            is FilterState.Complete -> todos.filter { it.state == Todo.STATE_COMPLETE }
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