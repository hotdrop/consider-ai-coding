package jp.hotdrop.considercline.android.ui.pointget

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.hotdrop.considercline.android.R
import jp.hotdrop.considercline.android.ui.theme.AppColor
import jp.hotdrop.considercline.android.ui.theme.ConsiderClineTheme
import androidx.hilt.navigation.compose.hiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun PointGetConfirmScreen(
    viewModel: PointGetViewModel = hiltViewModel(),
    onExecuteClick: () -> Unit
) {
    val inputPoint by viewModel.inputPoint.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text(stringResource(id = R.string.point_get_title)) })
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = stringResource(id = R.string.point_get_confirm_overview),
                style = MaterialTheme.typography.h6
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(id = R.string.point_get_confirm_detail),
                style = MaterialTheme.typography.body1
            )
            Spacer(modifier = Modifier.height(24.dp))
            Text(
                text = stringResource(id = R.string.point_get_confirm_point_label),
                style = MaterialTheme.typography.subtitle1
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "$inputPoint ${stringResource(id = R.string.point_unit)}",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = AppColor.PrimaryColor
            )
            Spacer(modifier = Modifier.height(32.dp))
            Button(onClick = onExecuteClick) {
                Text(text = stringResource(id = R.string.point_get_confirm_execute_button))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PointGetConfirmScreenPreview() {
    // Preview用のダミーViewModelを直接生成
    // PointGetViewModelのコンストラクタにデフォルト引数 KmpUseCaseFactory.get() があるため、
    // Previewでは引数なしで呼び出し可能
    val dummyViewModel = PointGetViewModel().apply {
        // 必要に応じてStateFlowの値を設定
        // (例) override val inputPoint: StateFlow<String> = MutableStateFlow("1000")
        // ただし、PointGetViewModelのinputPointはfinalなので直接overrideはできない。
        // PreviewのためだけにViewModelの構造を変えるのは避ける。
        // 現状のPointGetViewModelの実装では、inputPointはコンストラクタで初期化されるか、
        // initブロックや特定の関数呼び出しで値が設定される想定。
        // ここではデフォルトの "0" が表示されることになる。
        // もしPreviewで特定の値を表示したい場合は、ViewModel側の対応が必要。
    }
    ConsiderClineTheme {
        PointGetConfirmScreen(
            viewModel = dummyViewModel,
            onExecuteClick = {}
        )
    }
}
