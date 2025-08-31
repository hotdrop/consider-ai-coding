
import Foundation
import shared

class PointGetViewModel: ObservableObject {
    @Published var viewState: PointGetViewState = .loading
    @Published var acquireEventState: PointAcquireEventState?

    private let pointUseCase: PointUseCase
    // TODO　本来は設定ファイル（config？）から取得する
    private let maxPoint = 20000

    init(pointUseCase: PointUseCase = KmpFactory.shared.useCaseFactory.pointUseCase) {
        self.pointUseCase = pointUseCase
    }

    @MainActor
    func load() async {
        do {
            let point = try await pointUseCase.findForIos()
            viewState = .success(
                currentPoint: point,
                inputPoint: 0,
                errorMessage: nil,
                isEnableConfirm: false
            )
        } catch {
            viewState = .error(message: "ポイント残高の取得に失敗しました。")
        }
    }

    func inputPoint(newInputPoint: Int) {
        guard case let .success(currentPoint, _, _, _) = viewState else {
            return
        }

        let errorMessage = validateInput(
            input: newInputPoint,
            current: Int(currentPoint.balance),
            maxAvailablePoint: maxPoint
        )
        let isEnable = errorMessage == nil && newInputPoint > 0

        viewState = .success(
            currentPoint: currentPoint,
            inputPoint: newInputPoint,
            errorMessage: errorMessage,
            isEnableConfirm: isEnable
        )
    }

    @MainActor
    func acquirePoint(inputPoint: Int) async {
        do {
            try await pointUseCase.acquire(inputPoint: Int32(inputPoint))
            acquireEventState = .success
        } catch {
            acquireEventState = .error(message: "ポイントの獲得に失敗しました。")
        }
    }

    private func validateInput(input: Int, current: Int, maxAvailablePoint: Int) -> String? {
        if input <= 0 {
            return "0より大きい値を入力してください。"
        }
        if input + current > maxAvailablePoint {
            return "最大ポイントを超えています。"
        }
        return nil
    }
}

enum PointGetViewState: Equatable {
    case loading
    case success(currentPoint: Point, inputPoint: Int, errorMessage: String?, isEnableConfirm: Bool)
    case error(message: String)
}

enum PointAcquireEventState: Equatable {
    case success
    case error(message: String)
}

