package jp.hotdrop.considercline.android.ui.start

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import jp.hotdrop.considercline.android.R
import kotlinx.coroutines.delay
import org.koin.androidx.compose.koinViewModel

@Composable
fun SplashScreen(
    viewModel: SplashViewModel = koinViewModel(),
    onNavigateToHome: () -> Unit,
    onNavigateToStart: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val context = LocalContext.current
    
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(context.getString(R.string.splash_title)) }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when (uiState) {
                is SplashUiState.Loading -> LoadingContent()
                is SplashUiState.Error -> ErrorContent((uiState as SplashUiState.Error).message)
                is SplashUiState.Success -> {
                    val appSetting = (uiState as SplashUiState.Success).appSetting
                    if (appSetting.isInitialized()) {
                        LaunchedEffect(Unit) {
                            delay(1000)
                            onNavigateToHome()
                        }
                        LoadingContent(userId = appSetting.userId)
                    } else {
                        FirstTimeContent(onNavigateToStart)
                    }
                }
            }
        }
    }
}

@Composable
private fun LoadingContent(userId: String? = null) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.start),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(16.dp))
        CircularProgressIndicator()
        if (userId != null) {
            val context = LocalContext.current
            Spacer(modifier = Modifier.height(24.dp))
            Text(context.getString(R.string.splash_user_id_label) + userId)
        }
    }
}

@Composable
private fun ErrorContent(errorMessage: String) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        // TODO AlertDialogを実装する
//        AlertDialog.Builder(context)
//            .setMessage(errorMessage)
//            .setPositiveButton("OK") { _, _ ->
//                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//                    (context as? Activity)?.finishAndRemoveTask()
//                } else {
//                    (context as? Activity)?.finish()
//                }
//            }
//            .show()
    }
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(context.getString(R.string.splash_error_Label))
    }
}

@Composable
private fun FirstTimeContent(onNavigateToStart: () -> Unit) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Image(
            painter = painterResource(R.drawable.start),
            contentDescription = null
        )
        Spacer(modifier = Modifier.height(16.dp))
        Box(
            modifier = Modifier.weight(1f),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = onNavigateToStart,
                modifier = Modifier.padding(horizontal = 36.dp, vertical = 16.dp)
            ) {
                Text(context.getString(R.string.splash_first_time_button))
            }
        }
    }
}
