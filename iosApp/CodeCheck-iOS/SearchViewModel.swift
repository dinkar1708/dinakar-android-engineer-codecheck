//
//  SearchViewModel.swift
//  CodeCheck-iOS
//
//  ViewModel driving the SwiftUI search screen using KMP SharedCore repository.
//

import Foundation
import SwiftUI
import Combine
import shared_core

// Conform DomainRepositoryItem to Identifiable for SwiftUI List
extension DomainRepositoryItem: @retroactive Identifiable {
    public var id: String { name }
}

@MainActor
final class SearchViewModel: ObservableObject {
    @Published var query: String = "kotlin"
    @Published var repositories: [DomainRepositoryItem] = []
    @Published var isLoading: Bool = false
    @Published var errorMessage: String? = nil
    @Published var flavor: AppFlavor = .mock

    var isMockMode: Bool { flavor == .mock }

    private let repository: DomainGitHubRepository

    init(repository: DomainGitHubRepository? = nil) {
        if let repo = repository {
            self.repository = repo
            self.flavor = .prod
        } else if ProcessInfo.processInfo.arguments.contains("-mock") || ProcessInfo.processInfo.environment["APP_FLAVOR"] == "mock" {
            self.repository = SharedCore.shared.createMockRepository(simulatedDelayMs: 300)
            self.flavor = .mock
            print("🚀 [CodeCheck-iOS] Initialized in OFFLINE MOCK Mode")
        } else if ProcessInfo.processInfo.arguments.contains("-dev") || ProcessInfo.processInfo.environment["APP_FLAVOR"] == "dev" {
            self.repository = SharedCore.shared.createLiveRepository()
            self.flavor = .dev
            print("🚀 [CodeCheck-iOS] Initialized in DEV Mode")
        } else if ProcessInfo.processInfo.arguments.contains("-stg") || ProcessInfo.processInfo.environment["APP_FLAVOR"] == "stg" {
            self.repository = SharedCore.shared.createLiveRepository()
            self.flavor = .stg
            print("🚀 [CodeCheck-iOS] Initialized in STG Mode")
        } else {
            self.repository = SharedCore.shared.createLiveRepository()
            self.flavor = .prod
            print("🚀 [CodeCheck-iOS] Initialized in PROD Mode")
        }
    }

    func clearSearch() {
        query = ""
        repositories = []
        errorMessage = nil
        isLoading = false
    }

    func search() async {
        let trimmed = query.trimmingCharacters(in: .whitespacesAndNewlines)
        guard !trimmed.isEmpty else {
            self.repositories = []
            return
        }

        isLoading = true
        errorMessage = nil

        let startTime = Date()

        do {
            let filter = DomainSearchFilter(language: nil, minStars: nil, updatedPeriod: "all", updatedAfter: nil)
            let result = try await repository.searchRepositories(query: trimmed, page: 1, sort: DomainSearchSort.bestMatch, filter: filter)
            let items = result.items
            let duration = String(format: "%.1f", Date().timeIntervalSince(startTime) * 1000.0)
            self.repositories = items
            self.isLoading = false
            print("✅ [CodeCheck-iOS] Loaded \(items.count) repos in \(duration)ms")
        } catch {
            self.errorMessage = error.localizedDescription
            self.isLoading = false
            print("❌ [CodeCheck-iOS] Search failed: \(error.localizedDescription)")
        }
    }
}
