import shared

protocol HistoryUseCaseProtocol {
    func findAll() async throws -> Array<PointHistory>
}

extension HistoryUseCase: HistoryUseCaseProtocol {}

class DummyHistoryUseCase: HistoryUseCaseProtocol {
    func findAll() async throws -> Array<PointHistory> {
        return []
    }
}
