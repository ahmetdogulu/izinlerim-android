package com.izinlerim.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import com.izinlerim.app.data.PackagePermissionScanner
import com.izinlerim.app.model.AppFilter
import com.izinlerim.app.model.AppPermissionInfo
import com.izinlerim.app.ui.components.ErrorState
import com.izinlerim.app.ui.components.LoadingState
import com.izinlerim.app.ui.screens.DetailScreen
import com.izinlerim.app.ui.screens.HomeScreen
import com.izinlerim.app.ui.theme.IzinlerimTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            IzinlerimTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    IzinlerimApp()
                }
            }
        }
    }
}

@Composable
private fun IzinlerimApp() {
    val context = LocalContext.current
    var apps by remember { mutableStateOf<List<AppPermissionInfo>>(emptyList()) }
    var selectedApp by remember { mutableStateOf<AppPermissionInfo?>(null) }
    var selectedFilter by remember { mutableStateOf(AppFilter.All) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var reloadKey by remember { mutableStateOf(0) }

    LaunchedEffect(reloadKey) {
        isLoading = true
        errorMessage = null
        runCatching {
            withContext(Dispatchers.Default) {
                PackagePermissionScanner(context).loadUserApps()
            }
        }.onSuccess { loadedApps ->
            apps = loadedApps
            isLoading = false
        }.onFailure { error ->
            errorMessage = error.localizedMessage ?: "Uygulamalar okunamadı."
            isLoading = false
        }
    }

    when {
        isLoading -> LoadingState()
        errorMessage != null -> ErrorState(
            message = errorMessage.orEmpty(),
            onRetry = { reloadKey++ }
        )
        selectedApp != null -> DetailScreen(
            app = selectedApp!!,
            onBack = { selectedApp = null }
        )
        else -> HomeScreen(
            apps = apps,
            selectedFilter = selectedFilter,
            onFilterSelected = { selectedFilter = it },
            onAppClick = { selectedApp = it }
        )
    }
}
