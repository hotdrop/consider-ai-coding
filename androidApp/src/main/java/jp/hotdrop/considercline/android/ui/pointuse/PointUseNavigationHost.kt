package jp.hotdrop.considercline.android.ui.pointuse

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

/**
 * ポイント利用機能のナビゲーションホスト。
 * PointUseInputScreenとPointUseConfirmScreen間の画面遷移を管理します。
 */
@Composable
fun PointUseNavigationHost(
    navController: NavHostController = rememberNavController(),
    viewModel: PointUseViewModel = hiltViewModel(),
    onClose: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = PointUseScreen.Input.route
    ) {
        composable(PointUseScreen.Input.route) {
            PointUseInputScreen(
                viewModel = viewModel,
                onNavigateToConfirm = { navController.navigate(PointUseScreen.Confirm.route) },
                onBack = onClose
            )
        }
        composable(PointUseScreen.Confirm.route) {
            PointUseConfirmScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onComplete = onNavigateToHome
            )
        }
    }
}

/**
 * ポイント利用機能の画面ルートを定義するsealed class。
 */
sealed class PointUseScreen(val route: String) {
    object Input : PointUseScreen("pointUseInput")
    object Confirm : PointUseScreen("pointUseConfirm")
}
