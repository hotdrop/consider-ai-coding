package jp.hotdrop.considercline.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import jp.hotdrop.considercline.android.ui.start.SplashScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MyApplicationTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "splash") {
                    composable("splash") {
                        SplashScreen(
                            onNavigateToHome = { navController.navigate("home") },
                            onNavigateToStart = { navController.navigate("start") }
                        )
                    }
                    // 他の画面のcomposableは後ほど実装
                }
            }
        }
    }
}
