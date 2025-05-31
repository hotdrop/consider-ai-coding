package jp.hotdrop.considercline.android.ui.pointget

import androidx.compose.runtime.Composable
import androidx.compose.runtime.Composable
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

        composable(confirmDestination) {
            PointGetConfirmScreen(
                // ViewModelはPointGetConfirmScreen内部でhiltViewModel()により取得される
                // onExecuteClickはPointGetConfirmScreen内部でviewModelの関数を呼ぶ
                // onCompleteはタスク4で実装. Activityのfinishを呼び出す
                onComplete = onNavigateToHome
            )
        }
    }
}
