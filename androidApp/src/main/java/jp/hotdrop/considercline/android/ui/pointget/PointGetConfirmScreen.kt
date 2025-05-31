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
    onComplete: () -> Unit // タスク4で利用
) {
    val inputPoint by viewModel.inputPoint.collectAsState()
    // TODO: viewModelからローディング状態やエラー状態を取得してUIに反映する

    // タスク4で実装するダイアログ表示のためのState
    // val showSuccessDialog by viewModel.showSuccessDialog.collectAsState()
    // val showErrorDialog by viewModel.showErrorDialog.collectAsState()

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
            Button(onClick = { viewModel.acquirePoint() }) { // viewModelの関数を直接呼び出す
                Text(text = stringResource(id = R.string.point_get_confirm_execute_button))
            }
        }
    }

    // TODO: タスク4でダイアログ表示を実装
    // if (showSuccessDialog) { AlertDialog(...) }
    // if (showErrorDialog) { AlertDialog(...) }
}

@Preview(showBackground = true)
@Composable
fun PointGetConfirmScreenPreview() {
    val dummyViewModel = PointGetViewModel().apply {
        // Preview用にinputPointの値を設定したい場合は、
        // PointGetViewModelにデバッグ用の関数を追加するか、
        // MutableStateFlowを直接操作できるような仕組みが必要。
        // ここではデフォルト値で表示される。
    }
    ConsiderClineTheme {
        PointGetConfirmScreen(
            viewModel = dummyViewModel,
            onComplete = {}
        )
    }
}
