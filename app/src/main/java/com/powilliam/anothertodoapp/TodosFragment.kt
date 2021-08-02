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
    ): View {
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
        viewModel.dispatch(event = TodosViewModelEvent.ChangeTodoState(todo))
    }

    private fun observeViewModelState() {
        viewModel.state.observe(viewLifecycleOwner) {
            todoAdapter.submitList(it.todos)
            binding.chipGroupFilter.check(it.filterState.res)
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
            val newFilterState = when (checked) {
                R.id.filter_all -> FilterState.All
                R.id.filter_incomplete -> FilterState.Incomplete
                R.id.filter_complete -> FilterState.Complete
                else -> FilterState.All
            }
            viewModel.dispatch(event = TodosViewModelEvent.ChangeFilterState(newFilterState))
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