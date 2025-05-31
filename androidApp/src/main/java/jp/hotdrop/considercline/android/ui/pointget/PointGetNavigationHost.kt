package jp.hotdrop.considercline.android.ui.pointget

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun PointGetNavigationHost(
    navController: NavHostController = rememberNavController(),
    onNavigateToHome: () -> Unit
) {
    val inputDestination = "pointGetInput"
    val confirmDestination = "pointGetConfirm"

    NavHost(
        navController = navController,
        startDestination = inputDestination
    ) {
        composable(inputDestination) {
            PointGetInputScreen(
                onNavigateToConfirm = {
                    // PointGetViewModelのinputPointはPointGetInputScreen側で更新されている
                    // ナビゲーション引数は不要
                    navController.navigate(confirmDestination)
                },
                onBack = { navController.popBackStack() } // PointGetActivityのfinishを呼び出すべきか確認
            )
        }

        composable(confirmDestination) { backStackEntry ->
            val parentEntry = remember(backStackEntry) {
                navController.getBackStackEntry(inputDestination)
            }
            val viewModel : PointGetViewModel = hiltViewModel(parentEntry)
            PointGetConfirmScreen(
                viewModel = viewModel,
                onComplete = onNavigateToHome
            )
        }
    }
}
