package jp.co.yumemi.android.codecheck.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Network DTO representing a repository owner.
 */
@Serializable
data class OwnerDto(
    @SerialName("login") val login: String? = null,
    @SerialName("avatar_url") val avatarUrl: String? = null
)
