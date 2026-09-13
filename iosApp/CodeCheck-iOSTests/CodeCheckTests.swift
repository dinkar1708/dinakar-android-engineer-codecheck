//
//  CodeCheckTests.swift
//  CodeCheck-iOSTests
//
//  Automated native unit tests validating KMP SharedCore integration.
//

import Testing
import shared_core

@MainActor
struct CodeCheckTests {

    @Test func testMockRepositoryReturnsRepositories() async throws {
        let repo = SharedCore.shared.createMockRepository(simulatedDelayMs: 0)
        let items = try await repo.searchRepositories(query: "kotlin")
        #expect(!items.isEmpty)
        #expect(items.first?.name != nil)
    }

    @Test func testMockRepositoryDetails() async throws {
        let repo = SharedCore.shared.createMockRepository(simulatedDelayMs: 0)
        let item = try await repo.getRepositoryDetails(owner: "JetBrains", repo: "kotlin")
        #expect(!item.name.isEmpty)
        #expect(!item.owner.login.isEmpty)
    }
}
