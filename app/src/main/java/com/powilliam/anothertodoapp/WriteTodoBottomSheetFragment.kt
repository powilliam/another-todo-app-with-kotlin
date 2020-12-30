package com.powilliam.anothertodoapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.powilliam.anothertodoapp.databinding.FragmentWriteTodoBottomSheetBinding

class WriteTodoBottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var binding: FragmentWriteTodoBottomSheetBinding

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
        setToolbarNavigationOnClickListener()
        setToolbarOnMenuItemClickListener()
    }

    private fun setToolbarNavigationOnClickListener() {
        binding.toolbar.setNavigationOnClickListener {
            navigateToTodosFragment {
                // TODO Clear text field value
                // TODO Close keyboard
            }
        }
    }

    private fun setToolbarOnMenuItemClickListener() {
        binding.toolbar.setOnMenuItemClickListener { menuItem: MenuItem ->
            when(menuItem.itemId) {
                R.id.done -> navigateToTodosFragment {
                    // TODO Create or Update todo
                    // TODO Clear text field value
                    // TODO Close keyboard
                }
                else -> false
            }
        }
    }

    private fun navigateToTodosFragment(callback: () -> Unit): Boolean {
        callback()
        val controller = findNavController()
        controller.navigate(R.id.action_writeTodoBottomSheetFragment_to_todosFragment)
        return true
    }
}