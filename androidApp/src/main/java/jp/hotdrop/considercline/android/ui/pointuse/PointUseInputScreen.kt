package jp.hotdrop.considercline.android.ui.pointuse

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.hotdrop.considercline.android.R
import jp.hotdrop.considercline.android.ui.theme.ConsiderClineTheme
import jp.hotdrop.considercline.android.ui.pointuse.UiState

/**
 * ポイント利用数を入力するためのUIコンポーネント。
 *
 * @param uiState PointUseViewModelから受け取ったUiState。
 * @param onAction ユーザーイベントを通知するためのコールバック関数。
 * @param paddingValues 親Composableから渡されるパディング値。
 */
@Composable
fun PointUseInputScreen(
    viewModel: PointUseViewModel,
    onNavigateToConfirm: () -> Unit,
    paddingValues: PaddingValues
) {
    val uiState by viewModel.uiState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(paddingValues)
    ) {
        when {
            uiState.isLoading -> LoadingView()
            uiState.errorMessage != null -> {
                val errorMessage = uiState.errorMessage ?: "" // スマートキャストのためにローカル変数に代入
                ErrorView(errorMessage = errorMessage)
            }
            else -> PointUseInputContent(
                uiState = uiState,
                onInputChanged = { viewModel.inputPoint(it) },
                onConfirmClick = {
                    onNavigateToConfirm()
                }
            )
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
 * エラー発生時の表示。
 *
 * @param errorMessage 表示するエラーメッセージ。
 */
@Composable
private fun ErrorView(errorMessage: String) {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = errorMessage,
            color = MaterialTheme.colors.error,
            fontSize = 16.sp
        )
    }
}

/**
 * ポイント利用入力画面のコンテンツ。
 *
 * @param uiState PointUseViewModelから受け取ったUiState。
 * @param onAction ユーザーイベントを通知するためのコールバック関数。
 */
@Composable
private fun PointUseInputContent(
    uiState: UiState,
    onInputChanged: (String) -> Unit,
    onConfirmClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.point_use_input_overview),
            style = MaterialTheme.typography.body1
        )
        Spacer(modifier = Modifier.height(8.dp))
        HoldPointLabel(holdPoint = uiState.currentPoint)
        Spacer(modifier = Modifier.height(16.dp))
        PointTextField(
            inputPoint = uiState.inputPoint,
            holdPoint = uiState.currentPoint,
            errorMessage = uiState.errorMessage,
            onInputChanged = onInputChanged
        )
        Spacer(modifier = Modifier.height(16.dp))
        NextButton(
            enableButton = uiState.errorMessage == null && uiState.inputPoint > 0,
            onConfirmClick = onConfirmClick
        )
    }
}

/**
 * 現在の保有ポイントを表示するラベル。
 *
 * @param holdPoint 現在の保有ポイント。
 */
@Composable
private fun HoldPointLabel(holdPoint: Int) {
    Text(
        text = holdPoint.toString(),
        style = MaterialTheme.typography.h5
    )
}

/**
 * ポイント入力用のテキストフィールド。
 *
 * @param inputPoint 入力されたポイント数。
 * @param holdPoint 現在の保有ポイント。
 * @param errorMessage エラーメッセージ。
 * @param onInputChanged 入力値が変更された際のコールバック。
 */
@Composable
private fun PointTextField(
    inputPoint: Int,
    holdPoint: Int,
    errorMessage: String?,
    onInputChanged: (String) -> Unit
) {
    TextField(
        value = if (inputPoint == 0) "" else inputPoint.toString(),
        onValueChange = onInputChanged,
        label = { Text(stringResource(id = R.string.point_use_iInput_text_field_label)) },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        isError = errorMessage != null,
        modifier = Modifier.fillMaxWidth(0.8f),
        singleLine = true
    )
    if (errorMessage != null) {
        Text(
            text = errorMessage,
            color = MaterialTheme.colors.error,
            style = MaterialTheme.typography.caption,
            modifier = Modifier.padding(start = 8.dp)
        )
    }
}

/**
 * 次へ進むボタン。
 *
 * @param enableButton ボタンが有効かどうか。
 * @param onConfirmClick ボタンがクリックされた際のコールバック。
 */
@Composable
private fun NextButton(
    enableButton: Boolean,
    onConfirmClick: () -> Unit
) {
    Button(
        onClick = onConfirmClick,
        enabled = enableButton,
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .height(56.dp)
    ) {
        Text(
            text = stringResource(id = R.string.point_use_input_confirm_button),
            fontSize = 18.sp
        )
    }
}

/**
 * PointUseInputScreenで発生するユーザーイベントを定義するsealed class。
 */
@Preview(showBackground = true)
@Composable
fun PreviewPointUseInputScreen() {
    ConsiderClineTheme {
        PointUseInputContent(
            uiState = UiState(
                currentPoint = 100,
                inputPoint = 50,
                errorMessage = null,
                isLoading = false
            ),
            onInputChanged = {},
            onConfirmClick = {},
        )
    }
}
