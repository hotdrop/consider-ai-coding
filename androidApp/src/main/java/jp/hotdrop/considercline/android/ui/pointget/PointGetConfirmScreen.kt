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
import androidx.compose.material.AlertDialog
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

@Composable
fun PointGetConfirmScreen(
    viewModel: PointGetViewModel,
    onComplete: () -> Unit
) {
    val inputPoint by viewModel.inputPoint.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf<Throwable?>(null) }

    LaunchedEffect(Unit) {
        viewModel.pointAcquisitionSuccess.collect {
            showSuccessDialog = true
        }
    }
    LaunchedEffect(Unit) {
        viewModel.pointAcquisitionError.collect {
            showErrorDialog = it
        }
    }

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
            Button(
                onClick = { viewModel.acquirePoint() },
                enabled = !isLoading // ローディング中は無効
            ) {
                if (isLoading) {
                    // todo java.lang.NoSuchMethodError
//                    CircularProgressIndicator()
                } else {
                    Text(text = stringResource(id = R.string.point_get_confirm_execute_button))
                }
            }
        }
    }

    if (showSuccessDialog) {
        AlertDialog(
            onDismissRequest = {
                showSuccessDialog = false
                onComplete() // ダイアログを閉じたら完了処理
            },
            title = { Text(text = stringResource(id = R.string.point_get_title)) },
            text = { Text(text = stringResource(id = R.string.point_get_confirm_complete_dialog_message)) },
            confirmButton = {
                Button(onClick = {
                    showSuccessDialog = false
                    onComplete() // ダイアログを閉じたら完了処理
                }) {
                    Text(stringResource(id = R.string.dialog_ok))
                }
            }
        )
    }

    showErrorDialog?.let { error ->
        AlertDialog(
            onDismissRequest = { showErrorDialog = null },
            title = { Text(text = stringResource(id = R.string.splash_error_label)) }, // 汎用エラータイトル
            text = { Text(text = error.message ?: stringResource(id = R.string.home_loading_error_label)) }, // エラーメッセージ表示
            confirmButton = {
                Button(onClick = { showErrorDialog = null }) {
                    Text(stringResource(id = R.string.dialog_ok))
                }
            }
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PointGetConfirmScreenPreview() {
    ConsiderClineTheme {
        PointGetConfirmScreen(
            viewModel = PointGetViewModel(),
            onComplete = {}
        )
    }
}
