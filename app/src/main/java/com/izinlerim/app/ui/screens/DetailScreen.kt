package com.izinlerim.app.ui.screens

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.izinlerim.app.model.AppPermissionInfo
import com.izinlerim.app.model.PermissionGrantStatus
import com.izinlerim.app.model.PermissionCategory
import com.izinlerim.app.model.PermissionItem
import com.izinlerim.app.ui.components.AppIconImage
import com.izinlerim.app.ui.components.EmptyState
import com.izinlerim.app.ui.components.PermissionStatusBadge
import com.izinlerim.app.ui.components.RiskBadge
import com.izinlerim.app.ui.components.SensitiveBadge

@Composable
fun DetailScreen(
    app: AppPermissionInfo,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val visiblePermissions = remember(app) { app.userVisiblePermissions }
    val groupedPermissions = remember(visiblePermissions) { visiblePermissions.groupForDisplay() }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding(),
        contentPadding = PaddingValues(start = 20.dp, end = 20.dp, bottom = 24.dp)
    ) {
        item {
            DetailHeader(
                app = app,
                onBack = onBack,
                onOpenSettings = {
                    val intent = Intent(
                        Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                        Uri.parse("package:${app.packageName}")
                    )
                    context.startActivity(intent)
                }
            )
        }

        if (visiblePermissions.isEmpty()) {
            item {
                EmptyState(
                    title = "Kontrol edilebilir izin yok",
                    message = "Bu uygulamada kullanıcı tarafından açılıp kapatılabilen bir izin görünmüyor.",
                    modifier = Modifier.padding(top = 18.dp)
                )
            }
        } else {
            groupedPermissions.forEach { (category, permissions) ->
                item {
                    CategoryHeader(
                        category = category,
                        count = permissions.size,
                        modifier = Modifier.padding(top = 22.dp, bottom = 8.dp)
                    )
                }
                items(
                    items = permissions,
                    key = { permission -> permission.technicalName }
                ) { permission ->
                    PermissionRow(permission = permission)
                }
            }
        }
    }
}

@Composable
private fun DetailHeader(
    app: AppPermissionInfo,
    onBack: () -> Unit,
    onOpenSettings: () -> Unit
) {
    Column(
        modifier = Modifier.padding(top = 18.dp)
    ) {
        OutlinedButton(onClick = onBack) {
            Text("Geri")
        }
        Spacer(modifier = Modifier.height(14.dp))
        Card(
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        contentAlignment = Alignment.Center,
                        modifier = Modifier
                            .size(68.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.background)
                    ) {
                        AppIconImage(
                            icon = app.icon,
                            contentDescription = app.appName,
                            modifier = Modifier.size(50.dp)
                        )
                    }
                    Column(
                        verticalArrangement = Arrangement.spacedBy(4.dp),
                        modifier = Modifier
                            .weight(1f)
                            .padding(start = 16.dp)
                    ) {
                        Text(
                            text = app.appName,
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = app.appCategory.displayName,
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.64f),
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    RiskBadge(riskLevel = app.riskLevel)
                    Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                        Text(
                            text = "Risk puanı: ${app.riskScore}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.78f),
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "${app.sensitivePermissionCount} verilmiş hassas izin",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.SemiBold
                        )
                        Text(
                            text = "${app.userVisiblePermissions.size} kontrol edilebilir izin",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.64f)
                        )
                    }
                    Text(
                        text = app.riskExplanation,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.78f)
                    )
                    app.extraWarning?.let { warning ->
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(MaterialTheme.colorScheme.error.copy(alpha = 0.1f))
                                .padding(12.dp)
                        ) {
                            Text(
                                text = warning,
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.error,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
                Spacer(modifier = Modifier.height(18.dp))
                Button(
                    onClick = onOpenSettings,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Ayarları Aç")
                }
            }
        }
    }
}

@Composable
private fun CategoryHeader(
    category: PermissionCategory,
    count: Int,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.fillMaxWidth()
    ) {
        Text(
            text = category.displayName,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold
        )
        Text(
            text = "$count izin",
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.62f)
        )
    }
}

@Composable
private fun PermissionRow(permission: PermissionItem) {
    Card(
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 5.dp)
    ) {
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier.padding(14.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(3.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = permission.description,
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Medium
                )
            }
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.spacedBy(6.dp),
                modifier = Modifier.padding(start = 12.dp)
            ) {
                PermissionStatusBadge(grantStatus = permission.grantStatus)
                if (permission.isSensitive) {
                    SensitiveBadge()
                }
            }
        }
    }
}

private fun List<PermissionItem>.groupForDisplay(): List<Pair<PermissionCategory, List<PermissionItem>>> {
    val order = listOf(
        PermissionCategory.Camera,
        PermissionCategory.Microphone,
        PermissionCategory.Location,
        PermissionCategory.Contacts,
        PermissionCategory.Calendar,
        PermissionCategory.Sms,
        PermissionCategory.Phone,
        PermissionCategory.Storage,
        PermissionCategory.Other
    )
    val grouped = filter { permission ->
        permission.grantStatus != PermissionGrantStatus.SystemManaged
    }.groupBy { permission -> permission.category }
    return order.mapNotNull { category ->
        grouped[category]?.let { permissions -> category to permissions }
    }
}
