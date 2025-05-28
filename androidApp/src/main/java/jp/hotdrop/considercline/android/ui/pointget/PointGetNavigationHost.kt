package jp.hotdrop.considercline.android.ui.pointget

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@Composable
fun PointGetNavigationHost(
    navController: NavHostController = rememberNavController()
) {
    val inputDestination = "pointGetInput"
    val confirmDestination = "pointGetConfirm"

    NavHost(
        navController = navController,
        startDestination = inputDestination
    ) {
        composable(inputDestination) {
            PointGetInputScreen(
                onNavigateToConfirm = { point ->
                    navController.navigate("$confirmDestination/$point")
                },
                onBack = { navController.popBackStack() }
            )
        }

        composable(
            "$confirmDestination/{point}",
            arguments = listOf(navArgument("point") { type = NavType.IntType })
        ) { backStackEntry ->
            val point = backStackEntry.arguments?.getInt("point") ?: 0
            // TODO PointGetConfirmScreenを実装する
//            PointGetConfirmScreen(
//                point = point,
//                onBack = { navController.popBackStack() }
//            )
        }
    }
}