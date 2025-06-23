import shared

protocol PointUseCaseProtocol {
    func find() async throws -> Point
    func acquire(inputPoint: Int) async throws
    func use(inputPoint: Int) async throws
}

extension PointUseCase: PointUseCaseProtocol {}