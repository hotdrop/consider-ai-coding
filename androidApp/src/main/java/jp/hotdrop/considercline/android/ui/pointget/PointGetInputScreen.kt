package jp.hotdrop.considercline.android.ui.pointget

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
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.hotdrop.considercline.android.R
import jp.hotdrop.considercline.android.ui.theme.ConsiderClineTheme

/**
 * ポイント獲得数を入力するためのUIコンポーネント。
 */
@Composable
fun PointGetInputScreen(
    viewModel: PointGetViewModel,
    onNavigateToConfirm: () -> Unit,
    onBack: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { PointGetTopBar(onBack) },
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
                else -> PointGetInputContent(
                    uiState = uiState,
                    onInputChanged = { newValue ->
                        val context = LocalContext.current
                        val maxPoint = context.resources.getInteger(R.integer.max_point)
                        val maxAvailablePoint = uiState.currentPoint.getMaxAvailablePoint(maxPoint)
                        viewModel.inputPoint(newValue, maxAvailablePoint)
                    },
                    onNavigateToConfirm = onNavigateToConfirm
                )
            }
        }
    }
}

@Composable
fun PointGetTopBar(onBack: () -> Unit) {
    TopAppBar(
        title = {
            Box(
                modifier = Modifier.fillMaxWidth().padding(end = 48.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = stringResource(R.string.point_get_title),
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
 * ポイント獲得入力画面のコンテンツ
 */
@Composable
private fun PointGetInputContent(
    uiState: PointGetUiState,
    onInputChanged: (Int) -> Unit,
    onNavigateToConfirm: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        PointGetOverview(
            balance = uiState.currentPoint.balance,
            maxAvailable = uiState.currentPoint.getMaxAvailablePoint(
                LocalContext.current.resources.getInteger(R.integer.max_point)
            )
        )
        Spacer(modifier = Modifier.height(24.dp))
        PointGetInputField(
            inputPoint = uiState.inputPoint,
            showError = uiState.showError,
            maxLength = LocalContext.current.resources.getInteger(R.integer.max_point).toString().length,
            onValueChange = onInputChanged
        )
        Spacer(modifier = Modifier.height(32.dp))
        PointGetConfirmButton(
            isEnabled = uiState.isButtonEnabled,
            onNavigateToConfirm = onNavigateToConfirm
        )
    }
}

@Composable
fun PointGetOverview(balance: Int, maxAvailable: Int) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        Text(
            text = stringResource(R.string.point_get_input_overview),
            color = MaterialTheme.colors.primary,
            style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text = balance.toString(),
            color = MaterialTheme.colors.primary,
            style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(R.string.point_get_input_attention, maxAvailable),
            color = Color.Black,
            style = MaterialTheme.typography.body1
        )
    }
}

@Composable
fun PointGetInputField(
    inputPoint: Int,
    showError: Boolean,
    maxLength: Int,
    onValueChange: (Int) -> Unit
) {
    OutlinedTextField(
        value = if (inputPoint > 0) inputPoint.toString() else "",
        singleLine = true,
        onValueChange = { newValue ->
            if (newValue.length <= maxLength && newValue.all { it.isDigit() }) {
                val newValWithInt = newValue.toIntOrNull() ?: 0
                onValueChange(newValWithInt)
            }
        },
        label = { Text(stringResource(id = R.string.point_get_input_text_field_label)) },
        textStyle = TextStyle(color = Color.Black, fontSize = 20.sp),
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
        isError = showError,
        modifier = Modifier.fillMaxWidth()
    )
    if (showError) {
        Text(
            text = stringResource(R.string.point_get_input_max_over_error),
            color = Color.Red,
            style = MaterialTheme.typography.body2,
            modifier = Modifier.padding(start = 4.dp, top = 4.dp)
        )
    }
}

@Composable
fun PointGetConfirmButton(
    isEnabled: Boolean,
    onNavigateToConfirm: () -> Unit
) {
    Button(
        onClick = onNavigateToConfirm,
        enabled = isEnabled,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = stringResource(id = R.string.point_get_input_confirm_button),
            modifier = Modifier.padding(vertical = 8.dp)
        )
    }
}

@Preview(name = "PointGetInput - Loaded", showBackground = true)
@Composable
fun PreviewPointGetInputScreen() {
    ConsiderClineTheme {
        PointGetInputContent(
            uiState = PointGetUiState(
                currentPoint = jp.hotdrop.considercline.model.Point(100),
                inputPoint = 50,
                showError = false,
                isButtonEnabled = true,
                isLoading = false,
                errorMessage = null
            ),
            onInputChanged = {},
            onNavigateToConfirm = {}
        )
    }
}

@Preview(name = "PointGetInput - Loading", showBackground = true)
@Composable
fun PreviewPointGetInputScreen_Loading() {
    ConsiderClineTheme {
        LoadingView()
    }
}

@Preview(name = "PointGetInput - Error", showBackground = true)
@Composable
fun PreviewPointGetInputScreen_Error() {
    ConsiderClineTheme {
        ErrorView(errorMessage = "エラーが発生しました。")
    }
}
