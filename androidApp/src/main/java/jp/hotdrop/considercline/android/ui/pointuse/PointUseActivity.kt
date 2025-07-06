package jp.hotdrop.considercline.android.ui.pointuse

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import jp.hotdrop.considercline.android.ui.theme.ConsiderClineTheme
import androidx.navigation.compose.rememberNavController
import androidx.compose.material.Scaffold
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue

/**
 * ポイント利用機能全体のコンテナとなるActivity。
 * PointUseNavigationHostをホストする役割を担う。
 */
@AndroidEntryPoint
class PointUseActivity : ComponentActivity() {
    // HiltのviewModels()デリゲートを使用してPointUseViewModelのインスタンスを取得
    private val viewModel: PointUseViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val navController = rememberNavController()

            // ViewModelのisSuccessを監視し、成功したらActivityを終了
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()
            LaunchedEffect(uiState.isSuccess) {
                if (uiState.isSuccess) {
                    setResult(RESULT_OK)
                    finish()
                }
            }

            ConsiderClineTheme {
                Scaffold { paddingValues ->
                    PointUseNavigationHost(
                        navController = navController,
                        viewModel = viewModel,
                        paddingValues = paddingValues
                    )
                }
            }
        }
    }

    /**
     * HomeActivityから呼び出されることを想定し、処理完了後に結果を返すための基本的な仕組みをここに記述する。
     * 例:
     * override fun onBackPressed() {
     *     setResult(RESULT_OK)
     *     super.onBackPressed()
     * }
     */

    companion object {
        fun startForResult(context: Context, launcher: ActivityResultLauncher<Intent>) {
            val intent = Intent(context, PointUseActivity::class.java)
            launcher.launch(intent)
        }
    }
}