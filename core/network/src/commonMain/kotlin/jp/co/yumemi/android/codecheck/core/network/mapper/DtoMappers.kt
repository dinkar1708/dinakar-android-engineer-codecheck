package jp.co.yumemi.android.codecheck.core.network.mapper

import jp.co.yumemi.android.codecheck.core.domain.model.Owner
import jp.co.yumemi.android.codecheck.core.domain.model.RepositoryItem
import jp.co.yumemi.android.codecheck.core.network.model.OwnerDto
import jp.co.yumemi.android.codecheck.core.network.model.RepositoryItemDto

/**
 * Maps [OwnerDto] to pure domain [Owner].
 */
fun OwnerDto?.toDomain(): Owner {
    return Owner(
        avatarUrl = this?.avatarUrl.orEmpty(),
        login = this?.login.orEmpty()
    )
}

/**
 * Maps [RepositoryItemDto] to pure domain [RepositoryItem].
 */
fun RepositoryItemDto.toDomain(): RepositoryItem {
    val repoName = this.fullName ?: this.name.orEmpty()
    return RepositoryItem(
        name = repoName,
        owner = this.owner.toDomain(),
        language = this.language,
        stargazersCount = this.stargazersCount,
        watchersCount = this.watchersCount,
        forksCount = this.forksCount,
        openIssuesCount = this.openIssuesCount,
        description = this.description,
        htmlUrl = this.htmlUrl,
        updatedAt = this.pushedAt ?: this.updatedAt
    )
}
