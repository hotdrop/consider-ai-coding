import Foundation
import shared

public class IosUserDefaultsStore: KmpSharedPreferences {
    // MARK: — Keys
    private let userIdKey   = "key001"
    private let nickNameKey = "key002"
    private let emailKey    = "key003"
    private let pointKey    = "key004"
    private let jwtKey      = "key005"
    private let refreshTokenKey = "key006"

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
    public func getJwt() async throws -> String? {
        // JWTは暗号化すべき
        return UserDefaults.standard.string(forKey: jwtKey)
    }
    public func getRefreshToken() async throws -> String? {
        // 同様に要暗号化
        return UserDefaults.standard.string(forKey: refreshTokenKey)
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
    
    @discardableResult
    public func saveJwt(newVal: String) async throws -> KotlinUnit {
        UserDefaults.standard.set(newVal, forKey: jwtKey)
        return KotlinUnit()
    }
    
    public func saveJwt(newVal: String, completionHandler: @escaping ((any Error)?) -> Void) {
        Task {
            do {
                _ = try await saveJwt(newVal: newVal)
                completionHandler(nil)
            } catch {
                completionHandler(error)
            }
        }
    }
    
    @discardableResult
    public func saveRefreshToken(newVal: String) async throws -> KotlinUnit {
        UserDefaults.standard.set(newVal, forKey: refreshTokenKey)
        return KotlinUnit()
    }
    
    public func saveRefreshToken(newVal: String, completionHandler: @escaping ((any Error)?) -> Void) {
        Task {
            do {
                _ = try await saveRefreshToken(newVal: newVal)
                completionHandler(nil)
            } catch {
                completionHandler(error)
            }
        }
    }
}
