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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color

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
fun PointGetConfirmScreen(
    viewModel: PointGetViewModel,
    onBack: () -> Unit,
    onComplete: () -> Unit
) {
    val inputPoint by viewModel.inputPoint.collectAsState()
    var isLoading by remember { mutableStateOf(false) }
    var showSuccessDialog by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf<Throwable?>(null) }

    LaunchedEffect(viewModel.uiEventFlow) {
        viewModel.uiEventFlow.collect { event ->
            when (event) {
                PointAcquireEvent.NowLoading -> isLoading = true
                is PointAcquireEvent.ShowErrorDialog -> {
                    isLoading = false
                    showErrorDialog = event.throwable
                }
                PointAcquireEvent.ShowSuccessDialog -> {
                    isLoading = false
                    showSuccessDialog = true
                }
            }
        }
    }

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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            ) {
                PointConfirmOverview(inputPoint = inputPoint)
                Spacer(modifier = Modifier.height(32.dp))
                PointGetAcquireButton(isLoading = isLoading) {
                    viewModel.acquirePoint(inputPoint)
                }
            }
        }
    }

    if (showSuccessDialog) {
        SuccessDialog {
            showSuccessDialog = false
            onComplete()
        }
    }

    showErrorDialog?.let { error ->
        ErrorDialog(error) {
            showErrorDialog = null
        }
    }
}

@Composable
fun PointConfirmOverview(
    inputPoint: Int
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
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
            text = inputPoint.toString(),
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colors.primary
        )
    }
}

@Composable
fun PointGetAcquireButton(
    isLoading: Boolean,
    onClick: () -> Unit
) {
    // ローディング中は無効にする
    Button(
        onClick = onClick,
        enabled = !isLoading,
        modifier = Modifier.fillMaxWidth()
    ) {
        if (isLoading) {
            CircularProgressIndicator()
        } else {
            Text(
                text = stringResource(id = R.string.point_get_confirm_execute_button),
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}

@Composable
fun SuccessDialog(onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(id = R.string.point_get_title)) },
        text = { Text(text = stringResource(id = R.string.point_get_confirm_complete_dialog_message)) },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text(stringResource(id = R.string.dialog_ok))
            }
        }
    )
}

@Composable
fun ErrorDialog(error: Throwable, onDismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = stringResource(id = R.string.splash_error_label)) }, // 汎用エラータイトル
        text = { Text(text = error.message ?: stringResource(id = R.string.home_loading_error_label)) }, // エラーメッセージ表示
        confirmButton = {
            Button(onClick = onDismiss) {
                Text(stringResource(id = R.string.dialog_ok))
            }
        }
    )
}

@Preview(showBackground = true)
@Composable
fun PointGetConfirmScreenPreview() {
    ConsiderClineTheme {
        PointGetConfirmScreen(
            viewModel = PointGetViewModel(),
            onBack = {},
            onComplete = {}
        )
    }
}
