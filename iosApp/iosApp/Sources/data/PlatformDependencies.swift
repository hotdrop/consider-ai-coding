import Foundation
import shared

public class IosUserDefaultsStore: KmpSharedPreferences {
    // MARK: — Keys
    private let userIdKey   = "key001"
    private let nickNameKey = "key002"
    private let emailKey    = "key003"
    private let pointKey    = "key004"

    public func getUserId() async throws -> String? {
        return UserDefaults.standard.string(forKey: userIdKey)
    }
    public func getNickName() async throws -> String? {
        return UserDefaults.standard.string(forKey: nickNameKey)
    }
    public func getEmail() async throws -> String? {
        return UserDefaults.standard.string(forKey: emailKey)
    }
    public func getPoint() async throws -> KotlinInt {
        // UserDefaults.integer は未設定時 0 を返す
        return KotlinInt(integerLiteral: UserDefaults.standard.integer(forKey: pointKey))
    }

    @discardableResult
    public func saveUserId(newVal: String) async throws -> KotlinUnit {
        UserDefaults.standard.set(newVal, forKey: userIdKey)
        return KotlinUnit()
    }
    
    public func saveUserId(newVal: String, completionHandler: @escaping (Error?) -> Void) {
        Task {
            do {
                _ = try await saveUserId(newVal: newVal)
                completionHandler(nil)
            } catch {
                completionHandler(error)
            }
        }
    }
    
    @discardableResult
    public func saveNickName(newVal: String) async throws -> KotlinUnit {
        UserDefaults.standard.set(newVal, forKey: nickNameKey)
        return KotlinUnit()
    }
    
    public func saveNickName(newVal: String, completionHandler: @escaping (Error?) -> Void) {
        Task {
            do {
                _ = try await saveNickName(newVal: newVal)
                completionHandler(nil)
            } catch {
                completionHandler(error)
            }
        }
    }
    
    @discardableResult
    public func saveEmail(newVal: String) async throws -> KotlinUnit {
        UserDefaults.standard.set(newVal, forKey: emailKey)
        return KotlinUnit()
    }

    public func saveEmail(newVal: String, completionHandler: @escaping (Error?) -> Void) {
        Task {
            do {
                _ = try await saveEmail(newVal: newVal)
                completionHandler(nil)
            } catch {
                completionHandler(error)
            }
        }
    }
    
    @discardableResult
    public func savePoint(newVal: Int32) async throws -> KotlinUnit {
        UserDefaults.standard.set(newVal, forKey: pointKey)
        return KotlinUnit()
    }
    
    public func savePoint(newVal: Int32, completionHandler: @escaping ((any Error)?) -> Void) {
        Task {
            do {
                _ = try await savePoint(newVal: newVal)
                completionHandler(nil)
            } catch {
                completionHandler(error)
            }
        }
    }
}
