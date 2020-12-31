package com.powilliam.anothertodoapp.domain.daos

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.powilliam.anothertodoapp.domain.models.Todo

@Dao
interface TodoDao {
    @Query("SELECT * FROM todos")
    fun get(): LiveData<List<Todo>>

    @Query("UPDATE todos SET state = :state WHERE uuid = :uuid")
    fun updateState(uuid: String, state: Int)

    @Query("UPDATE todos SET content = :content WHERE uuid = :uuid")
    fun updateContent(uuid: String, content: String)

    @Insert
    fun create(vararg todos: Todo)

    @Delete
    fun delete(vararg todos: Todo)
}