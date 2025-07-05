package jp.hotdrop.considercline.android.ui.pointuse

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

/**
 * ポイント利用機能の画面ルートを定義するsealed class。
 * 型安全なナビゲーションを提供します。
 */
sealed class PointUseScreen(val route: String) {
    object Input : PointUseScreen("input")
    object Confirm : PointUseScreen("confirm")
}

/**
 * ポイント利用機能のナビゲーションホスト。
 * PointUseInputScreenとPointUseConfirmScreen間の画面遷移を管理します。
 *
 * @param navController ナビゲーションを制御するためのNavHostController。
 * @param viewModel PointUseViewModelのインスタンス。
 * @param paddingValues 親Composableから渡されるパディング値。
 */
@Composable
fun PointUseNavigationHost(
    navController: NavHostController,
    viewModel: PointUseViewModel,
    paddingValues: PaddingValues
) {
    NavHost(navController = navController, startDestination = PointUseScreen.Input.route) {
        composable(PointUseScreen.Input.route) {
            PointUseInputScreen(
                viewModel = viewModel,
                onNavigateToConfirm = { navController.navigate(PointUseScreen.Confirm.route) },
                paddingValues = paddingValues
            )
        }
        composable(PointUseScreen.Confirm.route) {
            PointUseConfirmScreen(
                viewModel = viewModel,
                onNavigateBack = { navController.popBackStack() }
            )
        }
    }
}