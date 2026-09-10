/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check

import android.os.Bundle
import android.view.View
import android.view.inputmethod.EditorInfo
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import jp.co.yumemi.android.code_check.databinding.FragmentRepositorySearchBinding
import jp.co.yumemi.android.code_check.extensions.setupWithLinearLayout

/**
 * Fragment for searching GitHub repositories
 * Displays a search input and list of repository results
 */
class RepositorySearchFragment : Fragment(R.layout.fragment_repository_search) {

    private val viewModel: RepositorySearchViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentRepositorySearchBinding.bind(view)

        // Setup adapter with lambda click handler
        val adapter = RepositoryAdapter { item ->
            navigateToRepositoryDetail(item)
        }

        // Observe search results from ViewModel
        viewModel.searchResults.observe(viewLifecycleOwner) { results ->
            adapter.submitList(results)
        }

        // Setup search input listener
        binding.searchInputText.setOnEditorActionListener { editText, action, _ ->
            if (action == EditorInfo.IME_ACTION_SEARCH) {
                viewModel.searchRepositories(editText.text.toString())
                true
            } else {
                false
            }
        }

        // Setup RecyclerView
        binding.recyclerView.apply {
            setupWithLinearLayout()
            this.adapter = adapter
        }
    }

    private fun navigateToRepositoryDetail(item: RepositoryItem) {
        val action = RepositorySearchFragmentDirections
            .actionRepositoriesFragmentToRepositoryFragment(item = item)
        findNavController().navigate(action)
    }
}
