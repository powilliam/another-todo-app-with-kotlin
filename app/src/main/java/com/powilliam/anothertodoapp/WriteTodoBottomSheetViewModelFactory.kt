package com.powilliam.anothertodoapp

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.powilliam.anothertodoapp.domain.daos.TodoDao
import java.lang.IllegalArgumentException

class WriteTodoBottomSheetViewModelFactory(private val todoDao: TodoDao) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return if (modelClass == WriteTodoBottomSheetViewModel::class.java) {
            WriteTodoBottomSheetViewModel(todoDao) as T
        } else {
            throw IllegalArgumentException(
                    "modelClass should be a WriteTodoBottomSheetViewModel::class.java"
            )
        }
    }
}