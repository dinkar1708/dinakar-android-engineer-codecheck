/*
 * Copyright © 2021 YUMEMI Inc. All rights reserved.
 */
package jp.co.yumemi.android.code_check.extensions

import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

/**
 * Setup RecyclerView with LinearLayoutManager and divider decoration
 */
fun RecyclerView.setupWithLinearLayout() {
    val linearLayoutManager = LinearLayoutManager(context)
    val dividerDecoration = DividerItemDecoration(context, linearLayoutManager.orientation)

    layoutManager = linearLayoutManager
    addItemDecoration(dividerDecoration)
}
