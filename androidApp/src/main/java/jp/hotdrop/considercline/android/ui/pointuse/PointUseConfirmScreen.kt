package jp.hotdrop.considercline.android.ui.pointuse

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.hotdrop.considercline.android.R
import jp.hotdrop.considercline.android.ui.theme.ConsiderClineTheme

/**
 * ポイント利用の確認画面。
 * 利用するポイント数を確認し、処理を確定するためのUIコンポーネント。
 *
 * @param viewModel PointUseViewModelのインスタンス。
 * @param onNavigateBack 入力画面に戻るためのコールバック。
 */
@Composable
fun PointUseConfirmScreen(
    viewModel: PointUseViewModel,
    onBack: () -> Unit,
    onComplete: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { PointConfirmTopBar(onBack) },
        backgroundColor = MaterialTheme.colors.primary
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> LoadingView()
                uiState.errorMessage != null -> {
                    ErrorDialog(
                        errorMessage = uiState.errorMessage ?: "",
                        onDismiss = { viewModel.updateUiState { copy(errorMessage = null) } }
                    )
                }
                uiState.isSuccess -> {
                    SuccessDialog(
                        onDismiss = {
                            viewModel.updateUiState { copy(isSuccess = false) }
                            onComplete()
                        }
                    )
                }
                else -> PointUseConfirmContent(
                    uiState = uiState,
                    onConfirmUsePoint = { viewModel.usePoint() }
                )
            }
        }
    }
}

@Composable
fun PointConfirmTopBar(onBack: () -> Unit) {
   TopAppBar(
       title = {
           Box(
               modifier = Modifier
                   .fillMaxWidth()
                   .padding(end = 48.dp),
               contentAlignment = Alignment.Center
           ) {
               Text(
                   text = stringResource(R.string.point_use_title),
               )
           }
       },
       navigationIcon = {
           IconButton(onClick = onBack) {
               Icon(
                   imageVector = Icons.Filled.ArrowBack,
                   contentDescription = stringResource(R.string.point_use_title),
               )
           }
       },
       backgroundColor = MaterialTheme.colors.primary,
       modifier = Modifier.statusBarsPadding()
   )
}

/**
* ポイント利用確認画面のコンテンツ。
*
* @param uiState PointUseViewModelから受け取ったUiState。
* @param onConfirmUsePoint ポイント利用を確定する際のコールバック。
*/
@Composable
private fun PointUseConfirmContent(
   uiState: UiState,
   onConfirmUsePoint: () -> Unit
) {
   Column(
       modifier = Modifier
           .fillMaxSize()
           .padding(16.dp),
       horizontalAlignment = Alignment.CenterHorizontally
   ) {
       Text(
           text = stringResource(id = R.string.point_use_confirm_overview),
           color = Color.Black,
           style = MaterialTheme.typography.h6,
       )
       Spacer(modifier = Modifier.height(8.dp))
       Text(
           text = stringResource(id = R.string.point_use_confirm_detail),
           color = Color.Black,
           style = MaterialTheme.typography.body1,
       )
       Spacer(modifier = Modifier.height(24.dp))
       Text(
           text = stringResource(id = R.string.point_use_confirm_point_label),
           style = MaterialTheme.typography.h5,
           color = MaterialTheme.colors.primary
       )
       Spacer(modifier = Modifier.height(8.dp))
       Text(
           text = uiState.inputPoint.toString(),
           fontSize = 32.sp,
           fontWeight = FontWeight.Bold,
           color = MaterialTheme.colors.primary
       )
       Spacer(modifier = Modifier.height(32.dp))
       Button(
           onClick = onConfirmUsePoint,
           enabled = !uiState.isLoading,
           modifier = Modifier.fillMaxWidth()
       ) {
           if (uiState.isLoading) {
               CircularProgressIndicator()
           } else {
               Text(
                   text = stringResource(id = R.string.point_use_confirm_execute_button),
                   modifier = Modifier.padding(vertical = 8.dp)
               )
           }
       }
   }
}

/**
* 読み込み中の表示。
*/
@Composable
private fun LoadingView() {
   Box(
       modifier = Modifier.fillMaxSize(),
       contentAlignment = Alignment.Center
   ) {
       CircularProgressIndicator()
   }
}

/**
* エラー発生時のダイアログ表示。
*
* @param errorMessage 表示するエラーメッセージ。
* @param onDismiss ダイアログが閉じられた際のコールバック。
*/
@Composable
private fun ErrorDialog(errorMessage: String, onDismiss: () -> Unit) {
   AlertDialog(
       onDismissRequest = onDismiss,
       title = { Text(stringResource(id = R.string.dialog_title_error)) },
       text = { Text(errorMessage) },
       confirmButton = {
           TextButton(onClick = onDismiss) {
               Text(stringResource(id = R.string.dialog_ok))
           }
       }
   )
}

/**
* 成功時のダイアログ表示。
*
* @param onDismiss ダイアログが閉じられた際のコールバック。
*/
@Composable
private fun SuccessDialog(onDismiss: () -> Unit) {
   AlertDialog(
       onDismissRequest = onDismiss,
       title = { Text(stringResource(id = R.string.dialog_title_success)) },
       text = { Text(stringResource(id = R.string.point_use_success_message)) },
       confirmButton = {
           TextButton(onClick = onDismiss) {
               Text(stringResource(id = R.string.dialog_ok))
           }
       }
   )
}

@Preview(showBackground = true)
@Composable
fun PreviewPointUseConfirmContent() {
   ConsiderClineTheme {
       PointUseConfirmContent(
           uiState = UiState(inputPoint = 100),
           onConfirmUsePoint = {}
       )
   }
}

@Preview(showBackground = true)
@Composable
fun PreviewPointUseConfirmContentLoading() {
   ConsiderClineTheme {
       LoadingView()
   }
}

@Preview(showBackground = true)
@Composable
fun PreviewPointUseConfirmContentError() {
   ConsiderClineTheme {
       ErrorDialog(errorMessage = "エラーが発生しました。", onDismiss = {})
   }
}

@Preview(showBackground = true)
@Composable
fun PreviewPointUseConfirmContentSuccess() {
   ConsiderClineTheme {
       SuccessDialog(onDismiss = {})
   }
}