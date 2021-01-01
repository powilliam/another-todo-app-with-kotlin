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
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WriteTodoBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentWriteTodoBottomSheetBinding
    private val viewModel: WriteTodoBottomSheetViewModel by viewModels()
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
        setDeleteButtonVisibilityWhenHavingTodoAsNavigationArgument()
        setToolbarNavigationOnClickListener()
        setToolbarOnMenuItemClickListener()
    }

    private fun setContentTextfieldValueWhenHavingTodoAsNavigationArgument() {
        args.todo?.let {
            binding.textField.text.append(it.content)
        }
    }

    private fun setDeleteButtonVisibilityWhenHavingTodoAsNavigationArgument() {
        val deleteMenuItem = binding.toolbar.menu.getItem(DELETE_MENU_ITEM)
        when(args.todo) {
            null -> {
                deleteMenuItem.isVisible = false
                deleteMenuItem.isEnabled = false
            }
            else -> {
                deleteMenuItem.isVisible = true
                deleteMenuItem.isEnabled = true
            }
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
                R.id.delete -> navigateToTodosFragment {
                    args.todo?.let {
                        viewModel.deleteTodo(it)
                    }
                }
                else -> false
            }
        }
    }

    private fun navigateToTodosFragment(callback: () -> Unit): Boolean {
        try {
            val action = WriteTodoBottomSheetFragmentDirections
                    .actionWriteTodoBottomSheetFragmentToTodosFragment()
            val controller = findNavController()
            controller.navigate(action)
            return true
        } finally {
            callback()
        }
    }

    companion object {
        private const val DELETE_MENU_ITEM = 0
    }
}