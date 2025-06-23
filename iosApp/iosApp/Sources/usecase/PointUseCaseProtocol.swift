import shared

protocol PointUseCaseProtocol {
    func find() async throws -> Point
    func acquire(inputPoint: Int32) async throws
    func use(inputPoint: Int32) async throws
}

extension PointUseCase: PointUseCaseProtocol {}

class DummyPointUseCase: PointUseCaseProtocol {
    func find() async throws -> shared.Point {
        return shared.Point(balance: 1000)
    }
    func acquire(inputPoint: Int32) async throws {}
    func use(inputPoint: Int32) async throws {}
}
