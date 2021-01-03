package com.powilliam.anothertodoapp.ui.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.findNavController
import com.powilliam.anothertodoapp.R
import com.powilliam.anothertodoapp.databinding.FragmentWorkspacesBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class WorkspacesFragment : Fragment() {
    private lateinit var binding: FragmentWorkspacesBinding

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil
                .inflate(inflater, R.layout.fragment_workspaces, container, false)
        binding.lifecycleOwner = this
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setFabOnClickListener()
    }

    private fun setFabOnClickListener() {
        binding.fab.setOnClickListener {
            navigateToWriteWorkspaceFragment()
        }
    }

    private fun navigateToWriteWorkspaceFragment() {
        val title = getString(R.string.create_workspace)
        val action = WorkspacesFragmentDirections
                .actionWorkspacesFragmentToWriteWorkspacesFragment(title)
        val controller = findNavController()
        controller.navigate(action)
    }
}