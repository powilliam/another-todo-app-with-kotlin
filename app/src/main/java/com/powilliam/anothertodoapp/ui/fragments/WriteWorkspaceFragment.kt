package com.powilliam.anothertodoapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.powilliam.anothertodoapp.R
import com.powilliam.anothertodoapp.databinding.FragmentWriteWorkspaceBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WriteWorkspaceFragment : Fragment() {
    private lateinit var binding: FragmentWriteWorkspaceBinding
    private val args: WriteWorkspaceFragmentArgs by navArgs()

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_write_workspace, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setToolbarTitle()
        setDeleteButtonVisibility()
        setNavigationIconOnClickListener()
        setToolbarOnMenuItemClickListener()
    }

    private fun setToolbarTitle() {
        binding.toolbar.title = args.title
    }

    private fun setDeleteButtonVisibility() {
        binding.toolbar.menu
                .getItem(DELETE_MENU_ITEM)
                .apply {
                    isVisible = false
                    isEnabled = false
                }
    }

    private fun setNavigationIconOnClickListener() {
        binding.toolbar.setNavigationOnClickListener {
            navigateToWorkspacesFragment()
        }
    }

    private fun setToolbarOnMenuItemClickListener() {
        binding.toolbar.setOnMenuItemClickListener { menuItem: MenuItem ->
            when(menuItem.itemId) {
                R.id.done -> navigateToTodosFragment {}
                else -> false
            }
        }
    }

    private fun navigateToWorkspacesFragment() {
        val action = WriteWorkspaceFragmentDirections
                .actionWriteWorkspaceFragmentToWorkspacesFragment()
        val controller = findNavController()
        controller.navigate(action)
    }

    private fun navigateToTodosFragment(callback: () -> Unit): Boolean {
        try {
            val action = WriteWorkspaceFragmentDirections
                    .actionWriteWorkspaceFragmentToTodosFragment()
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