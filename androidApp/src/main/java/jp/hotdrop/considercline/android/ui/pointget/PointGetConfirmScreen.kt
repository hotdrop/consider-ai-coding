package jp.hotdrop.considercline.android.ui.pointget

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
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
import jp.hotdrop.considercline.android.ui.theme.ConsiderClineTheme
import androidx.compose.material.AlertDialog
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.TextButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.ui.graphics.Color
import jp.hotdrop.considercline.model.AppError

@Composable
fun PointGetConfirmScreen(
    viewModel: PointGetViewModel,
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
            PointGetConfirmContent(
                uiState = uiState,
                onAcquirePoint = { viewModel.acquirePoint(uiState.inputPoint) },
                errorDialogDismiss = { viewModel.resetAcquireEvent() },
                onComplete = {
                    viewModel.resetAcquireEvent()
                    onComplete()
                }
            )
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
                    text = stringResource(R.string.point_get_title),
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.point_get_confirm_back_button_content_description),
                )
            }
        },
        backgroundColor = MaterialTheme.colors.primary,
        modifier = Modifier.statusBarsPadding()
    )
}

@Composable
private fun PointGetConfirmContent(
    uiState: PointGetUiState,
    onAcquirePoint: () -> Unit,
    errorDialogDismiss: () -> Unit,
    onComplete: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(id = R.string.point_get_confirm_overview),
            color = Color.Black,
            style = MaterialTheme.typography.h6,
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = stringResource(id = R.string.point_get_confirm_detail),
            color = Color.Black,
            style = MaterialTheme.typography.body1,
        )
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = stringResource(id = R.string.point_get_confirm_point_label),
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
            onClick = onAcquirePoint,
            enabled = !uiState.runAcquiringProcess,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (uiState.runAcquiringProcess) {
                CircularProgressIndicator(color = MaterialTheme.colors.onPrimary)
            } else {
                Text(
                    text = stringResource(id = R.string.point_get_confirm_execute_button),
                    modifier = Modifier.padding(vertical = 8.dp)
                )
            }
        }

        when (val event = uiState.acquireEvent) {
            is PointAcquireEvent.ShowErrorDialog -> {
                ErrorDialog(
                    errorMessage = event.error.message,
                    onDismiss = errorDialogDismiss
                )
            }
            PointAcquireEvent.ShowSuccessDialog -> {
                SuccessDialog(
                    onDismiss = onComplete
                )
            }
            null -> { /** 何もしない */ }
        }
    }
}

@Composable
private fun LoadingView() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}

@Composable
private fun ErrorDialog(errorMessage: String, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(id = R.string.point_get_confirm_error)) },
        text = { Text(errorMessage) },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(id = R.string.dialog_ok))
            }
        }
    )
}

@Composable
private fun SuccessDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(stringResource(id = R.string.point_get_title)) },
        text = { Text(stringResource(id = R.string.point_get_confirm_complete_dialog_message)) },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text(stringResource(id = R.string.dialog_ok))
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun PreviewPointGetConfirmContent() {
    ConsiderClineTheme {
        PointGetConfirmContent(
            uiState = PointGetUiState(inputPoint = 100),
            onAcquirePoint = {},
            errorDialogDismiss = {},
            onComplete = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPointGetConfirmContentLoading() {
    ConsiderClineTheme {
        LoadingView()
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPointGetConfirmContentError() {
    val error = AppError.ProgramError("エラーが発生しました")
    ConsiderClineTheme {
        PointGetConfirmContent(
            uiState = PointGetUiState(
                inputPoint = 100,
                acquireEvent = PointAcquireEvent.ShowErrorDialog(error)
            ),
            onAcquirePoint = {},
            errorDialogDismiss = {},
            onComplete = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPointGetConfirmContentSuccess() {
    ConsiderClineTheme {
        SuccessDialog(onDismiss = {})
        PointGetConfirmContent(
            uiState = PointGetUiState(
                inputPoint = 100,
                acquireEvent = PointAcquireEvent.ShowSuccessDialog
            ),
            onAcquirePoint = {},
            errorDialogDismiss = {},
            onComplete = {}
        )
    }
}
