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

class TodoAdapter : ListAdapter<Todo, TodoAdapter.ViewHolder>(
        TodoAdapterDiffCallback()
) {
    override fun onCreateViewHolder(
            parent: ViewGroup, viewType: Int
    ): ViewHolder = ViewHolder.create(parent)

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val todo = getItem(position)
        holder.bind(todo)
    }

    class ViewHolder(private val binding: ListTodoItemBinding) : RecyclerView.ViewHolder(
            binding.root
    ) {
        fun bind(todo: Todo) {
            binding.todo = todo
            binding.executePendingBindings()
        }

        companion object {
            fun create(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = DataBindingUtil.inflate<ListTodoItemBinding>(
                        layoutInflater, R.layout.list_todo_item, parent, false)
                return ViewHolder(binding)
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