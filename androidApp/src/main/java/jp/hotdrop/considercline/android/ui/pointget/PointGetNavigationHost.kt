package jp.hotdrop.considercline.android.ui.pointget

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun PointGetNavigationHost(
    navController: NavHostController = rememberNavController(),
    viewModel: PointGetViewModel = hiltViewModel(),
    onClose: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    NavHost(
        navController = navController,
        startDestination = PointGetScreen.Input.route
    ) {
        composable(PointGetScreen.Input.route) {
            PointGetInputScreen(
                viewModel = viewModel,
                onNavigateToConfirm = { navController.navigate(PointGetScreen.Confirm.route) },
                onBack = onClose
            )
        }

        composable(PointGetScreen.Confirm.route) {
            PointGetConfirmScreen(
                viewModel = viewModel,
                onBack = { navController.popBackStack() },
                onComplete = onNavigateToHome
            )
        }
    }
}

/**
 * ポイント獲得機能の画面ルートを定義するsealed class。
 */
sealed class PointGetScreen(val route: String) {
    object Input : PointGetScreen("pointGetInput")
    object Confirm : PointGetScreen("pointGetConfirm")
}