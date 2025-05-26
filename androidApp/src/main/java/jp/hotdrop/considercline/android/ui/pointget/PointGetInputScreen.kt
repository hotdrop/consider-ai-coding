package jp.hotdrop.considercline.android.ui.pointget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
package jp.hotdrop.considercline.android.ui.pointget

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import jp.hotdrop.considercline.android.R
import jp.hotdrop.considercline.android.ui.theme.ConsiderClineTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PointGetInputScreen(
    viewModel: PointGetInputViewModel,
    onNavigateToConfirm: (Int) -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val inputPoint by viewModel.inputPoint.collectAsState()
    val showError by viewModel.showError.collectAsState()
    val isButtonEnabled by viewModel.isButtonEnabled.collectAsState()
    val currentPoint by viewModel.currentPoint.collectAsState()

    val maxPointFromRes = context.resources.getInteger(R.integer.max_point)
    val maxAvailablePoint = currentPoint.getMaxAvailablePoint(maxPointFromRes)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(id = R.string.point_get_input_title)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(id = R.string.back_button_description),
                            tint = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(id = R.string.point_get_input_label_current_point, currentPoint.balance),
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(id = R.string.point_get_input_label_max_available_point, maxAvailablePoint),
                style = MaterialTheme.typography.bodyLarge
            )
            Spacer(modifier = Modifier.height(16.dp))
            OutlinedTextField(
                value = inputPoint,
                onValueChange = { newValue ->
                    if (newValue.length <= 4 && newValue.all { it.isDigit() }) {
                        viewModel.onInputPointChanged(newValue, maxAvailablePoint)
                    }
                },
                label = { Text(stringResource(id = R.string.point_get_input_label_input_point)) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                isError = showError,
                modifier = Modifier.fillMaxWidth()
            )
            if (showError) {
                Text(
                    text = stringResource(id = R.string.point_get_input_max_over_error),
                    color = Color.Red,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(start = 4.dp, top = 4.dp)
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { onNavigateToConfirm(inputPoint.toInt()) },
                enabled = isButtonEnabled,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(id = R.string.point_get_input_button_confirm))
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPointGetInputScreen() {
    ConsiderClineTheme {
        // PreviewではViewModelのインスタンスを直接渡すことはできないため、ダミーのViewModelを渡すか、
        // 状態を直接渡すComposableを作成する必要があります。
        // ここでは簡易的にダミーのViewModelを渡していますが、実際のアプリではDIで提供されます。
        // TODO: 適切なPreviewの実装
        Text("PointGetInputScreen Preview")
    }
}
