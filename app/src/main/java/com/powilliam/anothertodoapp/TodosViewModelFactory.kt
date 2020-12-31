package com.powilliam.anothertodoapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.powilliam.anothertodoapp.domain.daos.TodoDao
import java.lang.IllegalArgumentException

class TodosViewModelFactory(private val todoDao: TodoDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass == TodosViewModel::class.java) {
            TodosViewModel(todoDao) as T
        } else {
            throw IllegalArgumentException("modelClass should be a TodosViewModel::class.java")
        }
    }
}