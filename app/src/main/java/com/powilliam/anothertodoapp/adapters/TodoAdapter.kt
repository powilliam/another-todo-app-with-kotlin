package com.powilliam.anothertodoapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.powilliam.anothertodoapp.R
import com.powilliam.anothertodoapp.domain.models.Todo
import com.powilliam.anothertodoapp.databinding.ListTodoItemBinding

typealias Pressable = (todo: Todo) -> Unit

class TodoAdapter(
        private val onPressTodoCard: Pressable,
        private val onPressTodoRadioButton: Pressable
) : ListAdapter<Todo, TodoAdapter.ViewHolder>(
        TodoAdapterDiffCallback()
) {
    override fun onCreateViewHolder(
            parent: ViewGroup, viewType: Int
    ): ViewHolder = ViewHolder.create(parent, onPressTodoCard, onPressTodoRadioButton)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val todo = getItem(position)
        holder.bind(todo)
    }

    class ViewHolder(
            private val binding: ListTodoItemBinding,
            private val onPressTodoCard: Pressable,
            private val onPressTodoRadioButton: Pressable
            ) : RecyclerView.ViewHolder(
            binding.root
    ) {
        fun bind(todo: Todo) {
            binding.todo = todo
            binding.executePendingBindings()
            binding.card.setOnClickListener { onPressTodoCard(todo) }
            binding.radiobutton.setOnClickListener { onPressTodoRadioButton(todo) }
        }

        companion object {
            fun create(
                    parent: ViewGroup,
                    onPressTodoCard: Pressable,
                    onPressTodoRadioButton: Pressable
            ): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = DataBindingUtil.inflate<ListTodoItemBinding>(
                        layoutInflater, R.layout.list_todo_item, parent, false)
                return ViewHolder(binding, onPressTodoCard, onPressTodoRadioButton)
            }
        }
    }
}

class TodoAdapterDiffCallback : DiffUtil.ItemCallback<Todo>() {
    override fun areItemsTheSame(
            oldItem: Todo, newItem: Todo
    ): Boolean = oldItem.uuid == newItem.uuid

    override fun areContentsTheSame(
            oldItem: Todo, newItem: Todo
    ): Boolean = oldItem == newItem
}