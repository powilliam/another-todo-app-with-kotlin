package com.powilliam.anothertodoapp

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.powilliam.anothertodoapp.databinding.FragmentTodosBinding

class TodosFragment : Fragment() {
    private lateinit var binding: FragmentTodosBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_todos, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbarOnMenuItemClickListener()
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
        val controller = findNavController()
        controller.navigate(R.id.action_todosFragment_to_writeTodoBottomSheetFragment)
        return true
    }
}