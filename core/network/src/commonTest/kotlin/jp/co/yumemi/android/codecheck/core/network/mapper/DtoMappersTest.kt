package jp.co.yumemi.android.codecheck.core.network.mapper

import jp.co.yumemi.android.codecheck.core.network.model.OwnerDto
import jp.co.yumemi.android.codecheck.core.network.model.RepositoryItemDto
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class DtoMappersTest {

    @Test
    fun ownerDto_toDomain_mapsAvatarUrl() {
        val dto = OwnerDto(avatarUrl = "https://example.com/avatar.png")
        val domain = dto.toDomain()

        assertEquals("https://example.com/avatar.png", domain.avatarUrl)
    }

    @Test
    fun nullOwnerDto_toDomain_handlesGracefully() {
        val dto: OwnerDto? = null
        val domain = dto.toDomain()

        assertEquals("", domain.avatarUrl)
    }

    @Test
    fun repositoryItemDto_toDomain_prefersFullNameOverName() {
        val dto = RepositoryItemDto(
            name = "short-name",
            fullName = "owner/full-name",
            owner = OwnerDto(avatarUrl = "https://example.com/avatar.png"),
            language = "Kotlin",
            stargazersCount = 10L,
            watchersCount = 5L,
            forksCount = 2L,
            openIssuesCount = 1L,
            description = "Description",
            htmlUrl = "https://example.com"
        )

        val domain = dto.toDomain()

        assertEquals("owner/full-name", domain.name)
        assertEquals("https://example.com/avatar.png", domain.ownerIconUrl)
        assertEquals("Kotlin", domain.language)
        assertEquals(10L, domain.stargazersCount)
    }

    @Test
    fun repositoryItemDto_toDomain_fallsBackToNameWhenFullNameIsNull() {
        val dto = RepositoryItemDto(
            name = "fallback-name",
            fullName = null,
            owner = null,
            language = null,
            stargazersCount = 0L,
            watchersCount = 0L,
            forksCount = 0L,
            openIssuesCount = 0L,
            description = null,
            htmlUrl = null
        )

        val domain = dto.toDomain()

        assertEquals("fallback-name", domain.name)
        assertEquals("", domain.ownerIconUrl)
        assertNull(domain.language)
        assertNull(domain.description)
    }

    @Test
    fun repositoryItemDto_toDomain_mapsMetadataFields() {
        val dto = RepositoryItemDto(
            name = "fff",
            fullName = "dmtrKovalenko/fff",
            defaultBranch = "main",
            pushedAt = "2026-09-02T08:15:00Z",
            license = jp.co.yumemi.android.codecheck.core.network.model.LicenseDto(
                key = "mit",
                name = "MIT License",
                spdxId = "MIT"
            ),
            size = 4300L
        )

        val domain = dto.toDomain()

        assertEquals("main", domain.defaultBranch)
        assertEquals("2026-09-02T08:15:00Z", domain.pushedAt)
        assertEquals("MIT", domain.license)
        assertEquals(4300L, domain.size)
    }
}
