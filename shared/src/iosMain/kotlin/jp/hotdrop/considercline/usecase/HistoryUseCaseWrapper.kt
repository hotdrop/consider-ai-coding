package jp.hotdrop.considercline.usecase

import jp.hotdrop.considercline.model.AppResult
import jp.hotdrop.considercline.extension.SwiftBridgeException
import platform.Foundation.NSArray

suspend fun HistoryUseCase.findAllForSwift(): AppResult<NSArray> {
    return try {
        when (val result = findAll()) {
            is AppResult.Success -> {
                val nsArray = (result.data as List<*>).toTypedArray().toList() as NSArray
                AppResult.Success(nsArray)
            }
            is AppResult.Error -> AppResult.Error(result.exception)
        }
    } catch (e: Exception) {
        AppResult.Error(SwiftBridgeException(e.message ?: "Unknown error"))
    }
}