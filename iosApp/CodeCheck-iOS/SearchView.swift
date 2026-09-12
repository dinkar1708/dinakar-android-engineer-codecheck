//
//  SearchView.swift
//  CodeCheck-iOS
//
//  Simple, clean search list demonstrating Kotlin Multiplatform SharedCore integration.
//

import SwiftUI
import shared_core

struct SearchView: View {
    @StateObject private var viewModel = SearchViewModel()

    private var flavorBadgeColor: Color {
        switch viewModel.flavor {
        case .mock: return ColorTheme.appAmber
        case .dev:  return ColorTheme.appBlue
        case .stg:  return Color(red: 0.85, green: 0.45, blue: 0.1) // Staging orange-amber
        case .prod: return ColorTheme.appGreen
        }
    }

    var body: some View {
        NavigationStack {
            VStack(spacing: 0) {
                // Top Search Input Bar
                HStack(spacing: 8) {
                    HStack {
                        Image(systemName: "magnifyingglass")
                            .foregroundColor(ColorTheme.slate500)

                        TextField("Search GitHub repositories...", text: $viewModel.query)
                            .autocorrectionDisabled()
                            .textInputAutocapitalization(.never)
                            .submitLabel(.search)
                            .onSubmit {
                                Task { await viewModel.search() }
                            }

                        if !viewModel.query.isEmpty {
                            Button {
                                viewModel.clearSearch()
                            } label: {
                                Image(systemName: "xmark.circle.fill")
                                    .foregroundColor(ColorTheme.slate500)
                            }
                        }
                    }
                    .padding(10)
                    .background(ColorTheme.slate100)
                    .cornerRadius(10)

                    Button("Search") {
                        Task { await viewModel.search() }
                    }
                    .font(.subheadline)
                    .fontWeight(.semibold)
                    .padding(.horizontal, 14)
                    .padding(.vertical, 10)
                    .background(viewModel.isLoading || viewModel.query.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty ? ColorTheme.slate200 : ColorTheme.appBlue)
                    .foregroundColor(ColorTheme.appWhite)
                    .cornerRadius(10)
                    .disabled(viewModel.isLoading || viewModel.query.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty)
                }
                .padding(.horizontal)
                .padding(.top, 10)
                .padding(.bottom, 8)

                // Mode Indicator & Results Counter
                HStack {
                    Text(viewModel.flavor.badgeText)
                        .font(.system(size: 10, weight: .bold))
                        .padding(.horizontal, 6)
                        .padding(.vertical, 2)
                        .background(flavorBadgeColor.opacity(0.15))
                        .foregroundColor(flavorBadgeColor)
                        .cornerRadius(4)

                    Spacer()

                    if !viewModel.repositories.isEmpty {
                        Text("\(viewModel.repositories.count) repositories found")
                            .font(.caption2)
                            .fontWeight(.semibold)
                            .foregroundColor(ColorTheme.slate500)
                    }
                }
                .padding(.horizontal)
                .padding(.bottom, 6)

                Divider()

                // State Views
                if viewModel.isLoading {
                    Spacer()
                    VStack(spacing: 12) {
                        ProgressView()
                            .scaleEffect(1.2)
                            .tint(ColorTheme.appBlue)
                        Text("Searching via KMP Shared Engine...")
                            .font(.subheadline)
                            .foregroundColor(ColorTheme.slate500)
                    }
                    Spacer()
                } else if let error = viewModel.errorMessage {
                    Spacer()
                    VStack(spacing: 12) {
                        Image(systemName: "exclamationmark.triangle.fill")
                            .font(.system(size: 40))
                            .foregroundColor(ColorTheme.appAmber)
                        Text("Search Failed")
                            .font(.headline)
                            .foregroundColor(ColorTheme.slate900)
                        Text(error)
                            .font(.caption)
                            .foregroundColor(ColorTheme.slate500)
                            .multilineTextAlignment(.center)
                            .padding(.horizontal, 24)
                        Button("Retry") {
                            Task { await viewModel.search() }
                        }
                        .font(.subheadline)
                        .fontWeight(.semibold)
                        .padding(.horizontal, 16)
                        .padding(.vertical, 8)
                        .background(ColorTheme.appBlue)
                        .foregroundColor(ColorTheme.appWhite)
                        .cornerRadius(8)
                    }
                    .padding()
                    Spacer()
                } else if viewModel.repositories.isEmpty {
                    Spacer()
                    VStack(spacing: 8) {
                        Image(systemName: viewModel.query.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty ? "magnifyingglass" : "tray")
                            .font(.system(size: 44))
                            .foregroundColor(ColorTheme.slate500)
                        Text(viewModel.query.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty ? "Search GitHub Repositories" : "No Repositories Found")
                            .font(.headline)
                            .foregroundColor(ColorTheme.slate700)
                        Text(viewModel.query.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty ? "Type a repository name above to search via KMP" : "Try searching for \"kotlin\", \"android\", or \"swift\"")
                            .font(.caption)
                            .foregroundColor(ColorTheme.slate500)
                    }
                    Spacer()
                } else {
                    List(viewModel.repositories) { repo in
                        RepositoryRow(repository: repo)
                            .contentShape(Rectangle())
                            .onTapGesture {
                                print("👆 [CodeCheck-iOS] Clicked repository: \(repo.name) | Owner: \(repo.owner.login) | Stars: \(repo.stargazersCount)")
                            }
                    }
                    .listStyle(.plain)
                }
            }
            .navigationTitle("CodeCheck KMP")
            .navigationBarTitleDisplayMode(.inline)
            .onChange(of: viewModel.query) { newQuery in
                if newQuery.trimmingCharacters(in: .whitespacesAndNewlines).isEmpty {
                    viewModel.repositories = []
                }
            }
            .task {
                if viewModel.repositories.isEmpty {
                    await viewModel.search()
                }
            }
        }
    }
}

private struct RepositoryRow: View {
    let repository: DomainRepositoryItem

    var body: some View {
        HStack(alignment: .top, spacing: 12) {
            // Owner Avatar
            if let url = URL(string: repository.ownerIconUrl) {
                AsyncImage(url: url) { phase in
                    switch phase {
                    case .success(let image):
                        image
                            .resizable()
                            .aspectRatio(contentMode: .fill)
                    default:
                        Circle()
                            .fill(ColorTheme.slate200)
                            .overlay(
                                Text(String(repository.name.prefix(1)).uppercased())
                                    .font(.system(size: 14, weight: .bold))
                                    .foregroundColor(ColorTheme.slate700)
                            )
                    }
                }
                .frame(width: 44, height: 44)
                .clipShape(Circle())
            } else {
                Circle()
                    .fill(ColorTheme.slate200)
                    .frame(width: 44, height: 44)
            }

            // Info Column
            VStack(alignment: .leading, spacing: 4) {
                Text(repository.name)
                    .font(.system(size: 15, weight: .semibold))
                    .foregroundColor(ColorTheme.slate900)
                    .lineLimit(1)

                if let desc = repository.description_, !desc.isEmpty {
                    Text(desc)
                        .font(.system(size: 12))
                        .foregroundColor(ColorTheme.slate700)
                        .lineLimit(2)
                }

                HStack(spacing: 12) {
                    // Stars
                    HStack(spacing: 3) {
                        Image(systemName: "star.fill")
                            .font(.system(size: 10))
                            .foregroundColor(ColorTheme.appAmber)
                        Text("\(repository.stargazersCount)")
                            .font(.system(size: 11, weight: .medium))
                            .foregroundColor(ColorTheme.slate700)
                    }

                    // Forks
                    HStack(spacing: 3) {
                        Image(systemName: "tuningfork")
                            .font(.system(size: 10))
                            .foregroundColor(ColorTheme.slate500)
                        Text("\(repository.forksCount)")
                            .font(.system(size: 11, weight: .medium))
                            .foregroundColor(ColorTheme.slate500)
                    }

                    // Language Tag
                    if let language = repository.language, !language.isEmpty {
                        Text(language)
                            .font(.system(size: 10, weight: .semibold))
                            .padding(.horizontal, 6)
                            .padding(.vertical, 2)
                            .background(ColorTheme.appBlue.opacity(0.1))
                            .foregroundColor(ColorTheme.appBlue)
                            .cornerRadius(4)
                    }
                }
                .padding(.top, 2)
            }
        }
        .padding(.vertical, 4)
    }
}
