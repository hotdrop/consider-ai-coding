//
//  Extension.swift
//  iosApp
//
import Foundation
import shared

// Sealed Classが使いづらいのでAppResultをEnumにする
enum AppResultEx {
    case success(data: Any)
    case error(error: AppError)
}

func asEnumResult<T>(result: AppResult<T>) -> AppResultEx {
    if let success = result as? AppResultSuccess<T> {
        return .success(data: success.data as Any)
    }
    if let failure = result as? AppResultError {
        return .error(error: failure.error)
    }
    fatalError(#function + ": 想定外のAppResultです")
}

// Sealed Classが使いづらいのでAppCompleteをEnumにする
enum AppCompleteEx {
    case complete
    case error(error: AppError)
}

func asEnumComplete(complete: AppComplete) -> AppCompleteEx {
    if complete is AppComplete.Complete {
        return .complete
    }
    if let failure = complete as? AppComplete.Error {
        return .error(error: failure.error)
    }
    fatalError(#function + ": 想定外のAppCompleteです")
}

// KMPのBooleanをSwiftのBoolに変換する拡張関数（オプショナル型）
extension Optional where Wrapped == shared.KotlinBoolean {
    var asBool: Bool {
        return self?.boolValue ?? false
    }
}

// KMPのBooleanをSwiftのBoolに変換する拡張関数
extension shared.KotlinBoolean {
    var asBool: Bool {
        return self.boolValue
    }
}
