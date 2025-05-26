package jp.hotdrop.considercline.android.ui.pointget

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import dagger.hilt.android.AndroidEntryPoint
import jp.hotdrop.considercline.android.ui.theme.ConsiderClineTheme

@AndroidEntryPoint
class PointGetInputActivity : ComponentActivity() {

    private val viewModel: PointGetInputViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ConsiderClineTheme {
                PointGetInputScreen(
                    viewModel = viewModel,
                    onNavigateToConfirm = { point ->
                        val intent = Intent(this, PointGetConfirmActivity::class.java).apply {
                            putExtra(PointGetConfirmActivity.EXTRA_POINT_AMOUNT, point)
                        }
                        startActivity(intent)
                    },
                    onBack = { finish() }
                )
            }
        }
    }
}
