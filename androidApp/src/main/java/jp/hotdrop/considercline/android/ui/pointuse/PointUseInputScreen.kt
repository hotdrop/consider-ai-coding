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
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextField
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.hotdrop.considercline.android.R
import jp.hotdrop.considercline.android.ui.pointget.PointGetConfirmButton
import jp.hotdrop.considercline.android.ui.theme.ConsiderClineTheme
import jp.hotdrop.considercline.android.ui.pointuse.UiState

/**
 * ポイント利用数を入力するためのUIコンポーネント。
 *
 */
@Composable
fun PointUseInputScreen(
    viewModel: PointUseViewModel,
    onNavigateToConfirm: () -> Unit,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { PointUseTopBar(onBack) },
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
                uiState.errorMessage != null -> ErrorView(errorMessage = uiState.errorMessage ?: "")
                else -> PointUseInputContent(
                    uiState = uiState,
                    onInputChanged = { viewModel.inputPoint(it) },
                    onNavigateToConfirm = { onNavigateToConfirm() }
                )
            }
        }
    }
}

@Composable
fun PointUseTopBar(onBack: () -> Unit) {
    TopAppBar(
        title = {
            Box(
                modifier = Modifier.fillMaxWidth().padding(end = 48.dp),
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
                    contentDescription = stringResource(R.string.point_get_input_back_button_content_description),
                )
            }
        },
        backgroundColor = MaterialTheme.colors.primary,
        modifier = Modifier.statusBarsPadding()
    )
}

/**
 * 読み込み中の表示
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
 * エラー発生時の表示
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
 * ポイント利用入力画面のコンテンツ
 */
@Composable
private fun PointUseInputContent(
    uiState: UiState,
    onInputChanged: (Int) -> Unit,
    onNavigateToConfirm: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.point_use_input_overview),
            color = MaterialTheme.colors.primary,
            style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = uiState.currentPoint.toString(),
            color = MaterialTheme.colors.primary,
            style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(16.dp))
        PointTextField(
            inputPoint = uiState.inputPoint,
            holdPoint = uiState.currentPoint,
            errorMessage = uiState.errorMessage,
            onValueChange = onInputChanged
        )
        Spacer(modifier = Modifier.height(16.dp))
        PointUseConfirmButton(
            isEnabled = uiState.isEnableInputPoint,
            onNavigateToConfirm = onNavigateToConfirm
        )
    }
}

/**
 * ポイント入力用のテキストフィールド。
 */
@Composable
private fun PointTextField(
    inputPoint: Int,
    holdPoint: Int,
    errorMessage: String?,
    onValueChange: (Int) -> Unit
) {
    OutlinedTextField(
        value = if (inputPoint > 0) inputPoint.toString() else "",
        singleLine = true,
        onValueChange = { newValue ->
            if (newValue.isEmpty()) {
                onValueChange(0)
            } else if (newValue.all { it.isDigit() }) {
                // toLongOrNullで安全にパースし、保有ポイントと比較
                newValue.toLongOrNull()?.let {
                    if (it <= holdPoint) {
                        onValueChange(it.toInt())
                    }
                }
            }
        },
        label = { Text(stringResource(id = R.string.point_use_input_text_field_label)) },
        textStyle = TextStyle(color = Color.Black, fontSize = 20.sp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        isError = errorMessage != null,
        modifier = Modifier.fillMaxWidth()
    )
    if (errorMessage != null) {
        Text(
            text = errorMessage,
            color = Color.Red,
            style = MaterialTheme.typography.body2,
            modifier = Modifier.padding(start = 4.dp, top = 4.dp)
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
private fun PointUseConfirmButton(
    isEnabled: Boolean,
    onNavigateToConfirm: () -> Unit
) {
    Button(
        onClick = onNavigateToConfirm,
        enabled = isEnabled,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.point_use_input_confirm_button),
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}

@Preview(name = "PointUseInput - Loaded", showBackground = true)
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
            onNavigateToConfirm = {},
        )
    }
}
