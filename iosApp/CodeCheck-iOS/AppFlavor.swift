//
//  AppFlavor.swift
//  CodeCheck-iOS
//
//  Application flavor environments (Mock, Dev, Stg, Prod).
//

import Foundation

enum AppFlavor: String {
    case mock = "mock"
    case dev = "dev"
    case stg = "stg"
    case prod = "prod"

    var badgeText: String {
        switch self {
        case .mock: return "OFFLINE MOCK"
        case .dev:  return "DEV API"
        case .stg:  return "STG API"
        case .prod: return "PROD API"
        }
    }
}
