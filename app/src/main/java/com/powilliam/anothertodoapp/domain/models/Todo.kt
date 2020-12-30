package com.powilliam.anothertodoapp.domain.models

import java.util.*

data class Todo(
        val uuid: String = UUID
                .randomUUID()
                .toString(),
        val content: String,
        val state: Int = STATE_INCOMPLETE
) {
    companion object {
        const val STATE_INCOMPLETE = 0
        const val STATE_COMPLETE = 1
    }
}
