package jp.co.yumemi.android.codecheck.core.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

/**
 * Network DTO representing GitHub search repositories response payload.
 */
@Serializable
data class SearchResponseDto(
    @SerialName("total_count") val totalCount: Int = 0,
    @SerialName("incomplete_results") val incompleteResults: Boolean = false,
    @SerialName("items") val items: List<RepositoryItemDto> = emptyList()
)
