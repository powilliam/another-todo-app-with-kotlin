package com.powilliam.anothertodoapp.domain.databases

import androidx.room.Database
import androidx.room.RoomDatabase
import com.powilliam.anothertodoapp.domain.daos.TodoDao
import com.powilliam.anothertodoapp.domain.models.Todo

@Database(entities = [Todo::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun todoDao(): TodoDao

    companion object {
        const val DATABASE_NAME = "app-database"
    }
}