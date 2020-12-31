package com.powilliam.anothertodoapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.powilliam.anothertodoapp.adapters.TodoAdapter
import com.powilliam.anothertodoapp.databinding.FragmentTodosBinding
import com.powilliam.anothertodoapp.domain.databases.AppDatabase
import com.powilliam.anothertodoapp.domain.models.Todo

class TodosFragment : Fragment() {
    private lateinit var binding: FragmentTodosBinding
    private val todoAdapter = TodoAdapter(
            { todo: Todo ->
                val action = TodosFragmentDirections
                        .actionTodosFragmentToWriteTodoBottomSheetFragment(todo)
                val controller = findNavController()
                controller.navigate(action)
            },
            { todo: Todo ->
                viewModel.updateTodoState(todo)
            }
    )
    private val viewModel: TodosViewModel by viewModels {
        val database = AppDatabase.getInstance(requireContext())
        val todoDao = database.todoDao()
        TodosViewModelFactory(todoDao)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_todos, container, false)
        binding.lifecycleOwner = this
        binding.todoList.adapter = todoAdapter
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeViewModelState()
        setToolbarOnMenuItemClickListener()
    }

    private fun observeViewModelState() {
        viewModel.state.observe(viewLifecycleOwner) {
            todoAdapter.submitList(it.todos)
        }
    }

    private fun setToolbarOnMenuItemClickListener() {
        binding.toolbar.setOnMenuItemClickListener { menuItem: MenuItem ->
            when(menuItem.itemId) {
                R.id.write_todo -> navigateToWriteTodoBottomSheetFragment()
                else -> false
            }
        }
    }

    private fun navigateToWriteTodoBottomSheetFragment(): Boolean {
        val action = TodosFragmentDirections
                .actionTodosFragmentToWriteTodoBottomSheetFragment(null)
        val controller = findNavController()
        controller.navigate(action)
        return true
    }
}