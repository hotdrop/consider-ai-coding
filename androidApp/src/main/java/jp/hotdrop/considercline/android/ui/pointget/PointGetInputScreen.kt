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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
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
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import jp.hotdrop.considercline.android.R
import jp.hotdrop.considercline.android.ui.theme.ConsiderClineTheme

@Composable
fun PointGetInputScreen(
    viewModel: PointGetViewModel,
    onNavigateToConfirm: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val inputPoint by viewModel.inputPoint.collectAsState()
    val showError by viewModel.showError.collectAsState()
    val isButtonEnabled by viewModel.isButtonEnabled.collectAsState()
    val currentPoint by viewModel.currentPoint.collectAsState()

    val maxPoint = context.resources.getInteger(R.integer.max_point)
    val maxAvailablePoint = currentPoint.getMaxAvailablePoint(maxPoint)

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
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            ) {
                PointGetOverview(
                    balance = currentPoint.balance,
                    maxAvailable = maxAvailablePoint
                )
                Spacer(modifier = Modifier.height(24.dp))
                PointGetInputField(
                    inputPoint = inputPoint,
                    showError = showError,
                    maxLength = maxPoint.toString().length,
                    onValueChange = { newValue ->
                        viewModel.inputPoint(newValue, maxAvailablePoint)
                    }
                )
                Spacer(modifier = Modifier.height(32.dp))
                PointGetConfirmButton(
                    isEnabled = isButtonEnabled,
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

@Preview(showBackground = true)
@Composable
fun PreviewPointGetInputScreen() {
    ConsiderClineTheme {
        PointGetInputScreen(
            viewModel = PointGetViewModel(),
            onNavigateToConfirm = {},
            onBack = {}
        )
    }
}
