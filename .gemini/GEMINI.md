# Gemini Rules for E2E Test Server Project
This file defines the rules Gemini must follow when developing on this project.

## 1. Overall Rules
- Please answer in Japanese.
- Please provide a detailed explanation in Japanese.
- Please provide all the implementation code without omissions.
- Please clearly explain the justification and reason for the change, and the intention of the implementation.
- Please adhere to the basic principles of software development, such as the DRY principle, YAGNI, and SOLID principle. However, it is okay to prioritize the conventions of Android, Kotlin, iOS, and Swift over the principles.

## 2. Guidelines to be strictly observed
- When instructed to "Check the tasks," check `./nextTask.md`, make an implementation plan according to the Rules, and update `memory-bank/activeContext.md`.
- Adding libraries without permission is prohibited. Libraries are centrally managed using `gradle/libs.versions.toml`, so there is no problem reading this file, but if additions are required, be sure to check with the user.
- Do not add unnecessary comments to existing code. This makes it difficult for users to read the differences when reviewing.

## 3. Native layer and common logic prohibitions, priorities, and style rules
- When accessing common logic (`shared/`) from native implementations of Android and iOS (`androidApp/`, `iosApp/`), model classes in `model/` can be used freely.
- UseCase must be used via the `KmpUseCaseFactory` class. Use of any class other than the `KmpUseCaseFactory` class is prohibited. (For example, you must not instantiate the Repository class on your own)
- Compose must be used when implementing the UI of the Android native layer (`androidApp/`). AndroidView-based UI implementation is prohibited.
- SwiftUI must be used when implementing the UI of the iOS native layer (`iosApp/`). Swift interop layer implementation is prohibited. (Because it cannot take into account adjustments in Xcode or the Objective-C bridge)

## 4. Coding Standards
- Comply with the official Kotlin coding conventions (https://kotlinlang.org/docs/coding-conventions.html).
- Write KDoc comments for public classes, functions, and properties.
- `shared/`, the common logic, is a Kotlin native implementation. Be sure to implement it in Kotlin/Native as the JVM cannot be used.
- Android native `androidApp/` is a Kotlin implementation that runs under the Android SDK. Implement it in a Kotlin-like style while following Android conventions.
- iOS native `iosApp` is a Swift implementation. Implement it in a Swift-like style while following iOS conventions.
- Do not arbitrarily add unnecessary comments to existing code as it will make it difficult to see the differences.

## 5. Using Memory Bank
- When the user asks a question, be sure to read all Markdown files in the `memory-bank/` directory to fully understand the current task status and context.
- When you have finished planning your implementation and are moving to the implementation phase, or when an implementation task is completed, be sure to update `activeContext.md` to reflect the current work status without omission.
- **Strict adherence to step-by-step implementation**: Strictly adhere to the "must-follow" instructions in `activeContext.md` and implement the task one step at a time. After completing each step, get the user's confirmation before proceeding to the next step.
- **Reporting compilation errors**: Since Gemini cannot automatically detect iOS compilation errors, if an error occurs, the user should share the specific error message.
- **Update `activeContext.md`**: After completing each step, get the user's explicit approval before updating `activeContext.md`. Mark a step as complete only after the user's approval.

## 6. Handling confidential files and .Geminiignore
- Sensitive information: `.env` files, `.keystore` files, and files containing API keys, tokens, secrets, and passwords are never read or modified.
- `.Geminiignore`: No access is given to files and directories listed in the `.Geminiignore` file in the project root (build artifacts, sensitive files, OS-specific files, etc.). Files marked with 🔒 in `list_files` are forbidden.
