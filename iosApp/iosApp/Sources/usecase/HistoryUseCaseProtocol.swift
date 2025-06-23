import shared

protocol HistoryUseCaseProtocol {
    func findAll() async throws -> [History]
}

extension HistoryUseCase: HistoryUseCaseProtocol {}