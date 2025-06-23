import shared

protocol HistoryUseCaseProtocol {
    func findAll() async throws -> Array<PointHistory>
}

extension HistoryUseCase: HistoryUseCaseProtocol {}

class DummyHistoryUseCase: HistoryUseCaseProtocol {
    func findAll() async throws -> Array<PointHistory> {
        let dateTime = Kotlinx_datetimeLocalDateTime(
            year: 2025,
            monthNumber: 6,
            dayOfMonth: 22,
            hour: 15,
            minute: 25,
            second: 20,
            nanosecond: 0
        )
        return [
            PointHistory(dateTime: dateTime, point: 100, detail: "獲得"),
            PointHistory(dateTime: dateTime, point: -50, detail: "利用")
        ]
    }
}
