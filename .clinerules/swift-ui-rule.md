# SwiftUI View/UI Rules for iOS App (KMP Business Logic Integration)

This rule defines the architecture and coding conventions for the SwiftUI layer in an iOS application that integrates Kotlin Multiplatform (KMP) for business logic.

- You are an expert in coding with Swift and SwiftUI.
- Always write maintainable and clean code.
- Focus on the latest August, May 2025 version of the documentation and features.
- Descriptions should be short and concise.
- Don't remove any comments.
- Do not touch or redefine business logic from KMP.
- KMP logic is injected via interface bridges (e.g., shared ViewModel or UseCase protocols).
- This rule focuses only on SwiftUI Views and Swift-side ViewModels.

# SwiftUI Project structure: 
- Enforce the following SwiftUI project structure:
  - The main folder contains a "Sources" folder with:
    - "App" for main files
    - "Views" divided into "Home" and "Profile" sections with their ViewModels
    - "Shared" for reusable components and modifiers
  - "Models" for data models
  - "ViewModels" for view-specific logic
  - "Utilities" for extensions, constants, and helpers
  - The "Resources" folder holds:
    - "Assets" for images and colors
    - "Localization" for localized strings
    - "Fonts" for custom fonts
  - The "Tests" folder includes:
    - "UnitTests" for unit testing
    - "UITests" for UI testing

# SwiftUI UI Design Rules:
- Use Built-in Components: Utilize SwiftUI's native UI elements like List, NavigationView, TabView, and SF Symbols for a polished, iOS-consistent look.
- Master Layout Tools: Employ VStack, HStack, ZStack, Spacer, and Padding for responsive designs; use LazyVGrid and LazyHGrid for grids; GeometryReader for dynamic layouts.
- Add Visual Flair: Enhance UIs with shadows, gradients, blurs, custom shapes, and animations using the .animation() modifier for smooth transitions.
- Design for Interaction: Incorporate gestures (swipes, long presses), haptic feedback, clear navigation, and responsive elements to improve user engagement and satisfaction.