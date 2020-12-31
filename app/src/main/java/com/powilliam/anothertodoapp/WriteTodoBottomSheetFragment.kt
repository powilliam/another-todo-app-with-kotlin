package com.powilliam.anothertodoapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.powilliam.anothertodoapp.databinding.FragmentWriteTodoBottomSheetBinding
import com.powilliam.anothertodoapp.domain.databases.AppDatabase

class WriteTodoBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentWriteTodoBottomSheetBinding
    private val viewModel: WriteTodoBottomSheetViewModel by viewModels {
        val database = AppDatabase.getInstance(requireContext())
        val todoDao = database.todoDao()
        WriteTodoBottomSheetViewModelFactory(todoDao)
    }
    private val args: WriteTodoBottomSheetFragmentArgs by navArgs()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_write_todo_bottom_sheet, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setContentTextfieldValueWhenHavingTodoAsNavigationArgument()
        setToolbarNavigationOnClickListener()
        setToolbarOnMenuItemClickListener()
    }

    private fun setContentTextfieldValueWhenHavingTodoAsNavigationArgument() {
        args.todo?.let {
            binding.textField.setText(it.content)
        }
    }

    private fun setToolbarNavigationOnClickListener() {
        binding.toolbar.setNavigationOnClickListener {
            navigateToTodosFragment {
                binding.textField.clearComposingText()
            }
        }
    }

    private fun setToolbarOnMenuItemClickListener() {
        binding.toolbar.setOnMenuItemClickListener { menuItem: MenuItem ->
            when(menuItem.itemId) {
                R.id.done -> navigateToTodosFragment {
                    val content = binding.textField.text
                    if (content.isNotEmpty()) {
                        when (args.todo) {
                            null -> viewModel.createTodo(content.toString())
                            else -> viewModel
                                    .updateTodoContent(args.todo!!.uuid, content.toString())
                        }
                    }
                    binding.textField.clearComposingText()
                }
                else -> false
            }
        }
    }

    private fun navigateToTodosFragment(callback: () -> Unit): Boolean {
        try {
            val controller = findNavController()
            controller.navigate(R.id.action_writeTodoBottomSheetFragment_to_todosFragment)
            return true
        } finally {
            callback()
        }
    }
}