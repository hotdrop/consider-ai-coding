import shared

protocol HistoryUseCaseProtocol {
    func findAll() async throws -> AppResult<NSArray>
}

extension HistoryUseCase: HistoryUseCaseProtocol {
    func findAll() async throws -> AppResult<NSArray> {
        return try await self.findAllForSwift()
    }
}

class DummyHistoryUseCase: HistoryUseCaseProtocol {
    func findAll() async -> AppResult<NSArray> {
        return AppResultSuccess(data: [] as NSArray)
    }
}
