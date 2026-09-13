//
//  ColorTheme.swift
//  CodeCheck-iOS
//
//  Design palette matching the Android design system (:core:designsystem).
//

import SwiftUI

enum ColorTheme {
    // 6 Main Design Palette Colors
    static let appNavy = Color(red: 0x2D / 255.0, green: 0x35 / 255.0, blue: 0x45 / 255.0)   // Header, dark surfaces
    static let appBlue = Color(red: 0x3B / 255.0, green: 0x50 / 255.0, blue: 0xDF / 255.0)   // Action, links, focus
    static let appGreen = Color(red: 0x2D / 255.0, green: 0xB3 / 255.0, blue: 0x6C / 255.0)  // Positive, match
    static let appAmber = Color(red: 0xF5 / 255.0, green: 0x9E / 255.0, blue: 0x0B / 255.0)  // Stars, review
    static let appWhite = Color.white

    // Slate Neutral Ramp
    static let slate900 = Color(red: 0x0F / 255.0, green: 0x17 / 255.0, blue: 0x2A / 255.0)  // Primary text
    static let slate700 = Color(red: 0x33 / 255.0, green: 0x41 / 255.0, blue: 0x55 / 255.0)  // Secondary text
    static let slate500 = Color(red: 0x64 / 255.0, green: 0x74 / 255.0, blue: 0x8B / 255.0)  // Forks, meta counts
    static let slate200 = Color(red: 0xE2 / 255.0, green: 0xE8 / 255.0, blue: 0xF0 / 255.0)  // Borders, dividers
    static let slate100 = Color(red: 0xF1 / 255.0, green: 0xF5 / 255.0, blue: 0xF9 / 255.0)  // Background subtle
    static let slate50  = Color(red: 0xF5 / 255.0, green: 0xF6 / 255.0, blue: 0xF8 / 255.0)  // App background
}
