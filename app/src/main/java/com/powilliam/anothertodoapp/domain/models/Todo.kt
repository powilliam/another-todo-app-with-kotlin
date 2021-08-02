package com.powilliam.anothertodoapp.domain.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.*

@Entity(tableName = "todos")
data class Todo(
    @PrimaryKey val uuid: String = UUID
        .randomUUID()
        .toString(),
    @ColumnInfo val content: String,
    @ColumnInfo val state: Int = STATE_INCOMPLETE
) : Serializable {
    companion object {
        const val STATE_INCOMPLETE = 0
        const val STATE_COMPLETE = 1
    }
}
