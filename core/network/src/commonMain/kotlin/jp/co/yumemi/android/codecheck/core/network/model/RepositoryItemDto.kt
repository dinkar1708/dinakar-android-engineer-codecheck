package jp.co.yumemi.android.codecheck.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Network DTO representing a GitHub repository item.
 */
@Serializable
data class RepositoryItemDto(
    @SerialName("id") val id: Long = 0L,
    @SerialName("name") val name: String? = null,
    @SerialName("full_name") val fullName: String? = null,
    @SerialName("owner") val owner: OwnerDto? = null,
    @SerialName("language") val language: String? = null,
    @SerialName("stargazers_count") val stargazersCount: Long = 0L,
    @SerialName("watchers_count") val watchersCount: Long = 0L,
    @SerialName("forks_count") val forksCount: Long = 0L,
    @SerialName("open_issues_count") val openIssuesCount: Long = 0L,
    @SerialName("description") val description: String? = null,
    @SerialName("html_url") val htmlUrl: String? = null,
    @SerialName("updated_at") val updatedAt: String? = null,
    @SerialName("pushed_at") val pushedAt: String? = null
)
