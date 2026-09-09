/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import coil.load
import jp.co.yumemi.android.code_check.TopActivity.Companion.lastSearchDate
import jp.co.yumemi.android.code_check.databinding.FragmentTwoBinding

/**
 * Fragment for displaying detailed information about a GitHub repository
 */
class RepositoryDetailFragment : Fragment(R.layout.fragment_two) {

    private val args: RepositoryDetailFragmentArgs by navArgs()

    private var _binding: FragmentTwoBinding? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Log.d("LastSearchDate", lastSearchDate.toString())

        _binding = FragmentTwoBinding.bind(view)

        val item = args.item

        _binding?.apply {
            ownerIconView.load(item.ownerIconUrl)
            nameView.text = item.name
            languageView.text = item.language
            starsView.text = "${item.stargazersCount} stars"
            watchersView.text = "${item.watchersCount} watchers"
            forksView.text = "${item.forksCount} forks"
            openIssuesView.text = "${item.openIssuesCount} open issues"
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
