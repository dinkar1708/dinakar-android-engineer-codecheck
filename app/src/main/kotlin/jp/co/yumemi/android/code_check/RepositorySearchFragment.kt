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
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import jp.co.yumemi.android.code_check.databinding.FragmentRepositorySearchBinding

/**
 * Fragment for searching GitHub repositories
 * Displays a search input and list of repository results
 */
class RepositorySearchFragment : Fragment(R.layout.fragment_repository_search) {

    private val viewModel: RepositorySearchViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val binding = FragmentRepositorySearchBinding.bind(view)

        val layoutManager = LinearLayoutManager(requireContext())
        val dividerItemDecoration =
            DividerItemDecoration(requireContext(), layoutManager.orientation)
        val adapter = RepositoryAdapter(object : RepositoryAdapter.OnItemClickListener {
            override fun itemClick(item: RepositoryItem) {
                navigateToRepositoryDetail(item)
            }
        })

        // Observe search results from ViewModel
        viewModel.searchResults.observe(viewLifecycleOwner) { results ->
            adapter.submitList(results)
        }

        binding.searchInputText.setOnEditorActionListener { editText, action, _ ->
            if (action == EditorInfo.IME_ACTION_SEARCH) {
                val query = editText.text.toString()
                viewModel.searchRepositories(query)
                return@setOnEditorActionListener true
            }
            return@setOnEditorActionListener false
        }

        binding.recyclerView.apply {
            this.layoutManager = layoutManager
            addItemDecoration(dividerItemDecoration)
            this.adapter = adapter
        }
    }

    private fun navigateToRepositoryDetail(item: RepositoryItem) {
        val action = RepositorySearchFragmentDirections
            .actionRepositoriesFragmentToRepositoryFragment(item = item)
        findNavController().navigate(action)
    }
}
