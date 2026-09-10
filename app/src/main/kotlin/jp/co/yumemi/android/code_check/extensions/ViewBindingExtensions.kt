/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check.extensions

import coil.load
import jp.co.yumemi.android.code_check.RepositoryItem
import jp.co.yumemi.android.code_check.databinding.FragmentRepositoryDetailBinding

/**
 * Bind RepositoryItem data to the detail screen views
 */
fun FragmentRepositoryDetailBinding.bindRepositoryItem(item: RepositoryItem) {
    ownerIconView.load(item.ownerIconUrl)
    nameView.text = item.name
    languageView.text = item.language
    starsView.text = item.formatStars()
    watchersView.text = item.formatWatchers()
    forksView.text = item.formatForks()
    openIssuesView.text = item.formatOpenIssues()
}

/**
 * Format stars count for display
 */
fun RepositoryItem.formatStars(): String = "$stargazersCount stars"

/**
 * Format watchers count for display
 */
fun RepositoryItem.formatWatchers(): String = "$watchersCount watchers"

/**
 * Format forks count for display
 */
fun RepositoryItem.formatForks(): String = "$forksCount forks"

/**
 * Format open issues count for display
 */
fun RepositoryItem.formatOpenIssues(): String = "$openIssuesCount open issues"
