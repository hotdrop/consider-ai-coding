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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import jp.hotdrop.considercline.android.R
import jp.hotdrop.considercline.android.ui.theme.AppColor
import jp.hotdrop.considercline.android.ui.theme.ConsiderClineTheme

@Composable
fun PointGetInputScreen(
    viewModel: PointGetInputViewModel = hiltViewModel(),
    onNavigateToConfirm: (Int) -> Unit,
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
        backgroundColor = AppColor.PrimaryColor
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
                    inputPoint = inputPoint,
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
            Box(Modifier.fillMaxWidth().padding(end = 48.dp), Alignment.Center) {
                Text(
                    text = stringResource(R.string.point_get_title),
                    color = AppColor.White
                )
            }
        },
        navigationIcon = {
            IconButton(onClick = onBack) {
                Icon(
                    imageVector    = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.point_get_input_back_button_content_description),
                    tint           = AppColor.White
                )
            }
        },
        backgroundColor = AppColor.PrimaryColor,
        modifier        = Modifier.statusBarsPadding()
    )
}

@Composable
fun PointGetOverview(balance: Int, maxAvailable: Int) {
    Column(
        Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally)
    {
        Text(
            text  = stringResource(R.string.point_get_input_overview),
            color = MaterialTheme.colors.primary,
            style = MaterialTheme.typography.body1.copy(fontWeight = FontWeight.Bold)
        )
        Text(
            text  = balance.toString(),
            color = MaterialTheme.colors.primary,
            style = MaterialTheme.typography.h4.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(Modifier.height(8.dp))
        Text(
            text  = stringResource(R.string.point_get_input_attention, maxAvailable),
            color = AppColor.Black,
            style = MaterialTheme.typography.body1
        )
    }
}

@Composable
fun PointGetInputField(
    inputPoint: String,
    showError: Boolean,
    maxLength: Int,
    onValueChange: (String) -> Unit
) {
    OutlinedTextField(
        value = inputPoint,
        singleLine = true,
        onValueChange = { newValue ->
            if (newValue.length <= maxLength && newValue.all { it.isDigit() }) {
                onValueChange(newValue)
            }
        },
        label = { Text(stringResource(id = R.string.point_get_input_text_field_label)) },
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
    inputPoint: String,
    isEnabled: Boolean,
    onNavigateToConfirm: (Int) -> Unit
) {
    Button(
        onClick = { onNavigateToConfirm(inputPoint.toInt()) },
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
            viewModel = PointGetInputViewModel(),
            onNavigateToConfirm = {},
            onBack = {}
        )
    }
}
