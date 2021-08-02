package com.powilliam.anothertodoapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.powilliam.anothertodoapp.adapters.TodoAdapter
import com.powilliam.anothertodoapp.adapters.TodoCardClickListeners
import com.powilliam.anothertodoapp.databinding.FragmentTodosBinding
import com.powilliam.anothertodoapp.domain.models.Todo
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class TodosFragment : Fragment(),
    TodoCardClickListeners {
    private lateinit var binding: FragmentTodosBinding
    private val todoAdapter = TodoAdapter(this)
    private val viewModel: TodosViewModel by viewModels()

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
        setChipGroupFilterOnCheckedChangeListener()
    }

    override fun onPressTodoCard(todo: Todo) {
        navigateToWriteTodoBottomSheetFragment(todo)
    }

    override fun onPressTodoRadioButton(todo: Todo) {
        viewModel.updateTodoState(todo)
    }

    private fun observeViewModelState() {
        viewModel.state.observe(viewLifecycleOwner) {
            when (it.filterState) {
                TodosViewModel.FILTER_ALL -> {
                    todoAdapter.submitList(it.todos)
                    binding.chipGroupFilter.check(R.id.filter_all)
                }
                TodosViewModel.FILTER_IMCOMPLETE -> {
                    todoAdapter.submitList(it.incompleteTodos)
                    binding.chipGroupFilter.check(R.id.filter_incomplete)
                }
                TodosViewModel.FILTER_COMPLETE -> {
                    todoAdapter.submitList(it.completeTodos)
                    binding.chipGroupFilter.check(R.id.filter_complete)
                }
            }
        }
    }

    private fun setToolbarOnMenuItemClickListener() {
        binding.toolbar.setOnMenuItemClickListener { menuItem: MenuItem ->
            when (menuItem.itemId) {
                R.id.write_todo -> navigateToWriteTodoBottomSheetFragment(null)
                else -> false
            }
        }
    }

    private fun setChipGroupFilterOnCheckedChangeListener() {
        binding.chipGroupFilter.setOnCheckedChangeListener { _, checked ->
            when (checked) {
                R.id.filter_all -> viewModel
                    .updateFilterState(TodosViewModel.FILTER_ALL)
                R.id.filter_incomplete -> viewModel
                    .updateFilterState(TodosViewModel.FILTER_IMCOMPLETE)
                R.id.filter_complete -> viewModel
                    .updateFilterState(TodosViewModel.FILTER_COMPLETE)
            }
        }
    }

    private fun navigateToWriteTodoBottomSheetFragment(todo: Todo?): Boolean {
        val action = TodosFragmentDirections
            .actionTodosFragmentToWriteTodoBottomSheetFragment(todo)
        val controller = findNavController()
        controller.navigate(action)
        return true
    }
}