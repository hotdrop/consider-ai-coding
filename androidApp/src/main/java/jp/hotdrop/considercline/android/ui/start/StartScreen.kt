package jp.hotdrop.considercline.android.ui.start

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import jp.hotdrop.considercline.android.R
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun StartScreen(
    viewModel: StartViewModel = koinViewModel(),
    onNavigateToHome: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.start_title)) }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                Text(stringResource(R.string.start_overview))
                Spacer(modifier = Modifier.height(16.dp))
                
                OutlinedTextField(
                    value = uiState.nickName,
                    onValueChange = viewModel::onNickNameChanged,
                    label = { Text(stringResource(R.string.start_nick_name_field_label)) },
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                OutlinedTextField(
                    value = uiState.email,
                    onValueChange = viewModel::onEmailChanged,
                    label = { Text(stringResource(R.string.start_email_field_label)) },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth()
                )
                
                Spacer(modifier = Modifier.height(24.dp))
                
                Button(
                    onClick = {
                        scope.launch {
                            viewModel.save()
                            if (uiState.error == null) {
                                onNavigateToHome()
                            }
                        }
                    },
                    enabled = uiState.nickName.isNotEmpty() && uiState.email.isNotEmpty(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 36.dp)
                ) {
                    Text(stringResource(R.string.start_register_button))
                }
            }

            if (uiState.isLoading) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }

            uiState.error?.let {
                // TODO ダイアログ実装
//                AlertDialog(
//                    onDismissRequest = { viewModel.onErrorDismissed() },
//                    title = { Text(stringResource(R.string.error_dialog_title)) },
//                    text = { Text(error) },
//                    confirmButton = {
//                        TextButton(onClick = { viewModel.onErrorDismissed() }) {
//                            Text(stringResource(R.string.error_dialog_ok))
//                        }
//                    }
//                )
            }
        }
    }
}
