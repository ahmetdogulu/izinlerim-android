package com.izinlerim.app.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AssistChip
import androidx.compose.material3.AssistChipDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.izinlerim.app.model.AppFilter
import com.izinlerim.app.model.AppPermissionInfo
import com.izinlerim.app.model.PermissionCategory
import com.izinlerim.app.model.PermissionSummary
import com.izinlerim.app.model.RiskLevel
import com.izinlerim.app.ui.components.AppIconImage
import com.izinlerim.app.ui.components.EmptyState
import com.izinlerim.app.ui.components.RiskBadge
import com.izinlerim.app.ui.components.SummaryCard

@Composable
fun HomeScreen(
    apps: List<AppPermissionInfo>,
    selectedFilter: AppFilter,
    onFilterSelected: (AppFilter) -> Unit,
    onAppClick: (AppPermissionInfo) -> Unit
) {
    val summary = remember(apps) { apps.toSummary() }
    val filteredApps = remember(apps, selectedFilter) { apps.applyFilter(selectedFilter) }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .statusBarsPadding(),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            Header()
            SummarySection(summary)
            FilterSection(
                selectedFilter = selectedFilter,
                onFilterSelected = onFilterSelected
            )
            Text(
                text = "${filteredApps.size} uygulama",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 20.dp, vertical = 12.dp)
            )
        }

        when {
            apps.isEmpty() -> item {
                EmptyState(
                    title = "Listelenecek uygulama yok",
                    message = "Cihazda kullanıcı tarafından yüklenmiş ve başlatılabilir uygulama bulunamadı.",
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 24.dp)
                )
            }

            filteredApps.isEmpty() -> item {
                EmptyState(
                    title = "Bu filtre boş",
                    message = "Seçili kritere uyan uygulama yok. Başka bir filtre deneyebilirsiniz.",
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 24.dp)
                )
            }

            else -> items(
                items = filteredApps,
                key = { app -> app.packageName }
            ) { app ->
                AppListItem(
                    app = app,
                    onClick = { onAppClick(app) },
                    modifier = Modifier.padding(horizontal = 20.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
private fun Header() {
    Column(
        modifier = Modifier.padding(start = 20.dp, top = 22.dp, end = 20.dp, bottom = 14.dp)
    ) {
        Text(
            text = "İzinlerim",
            style = MaterialTheme.typography.headlineLarge,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = "Yüklü uygulamaların talep ettiği izinleri sade bir risk özetiyle inceleyin.",
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.72f)
        )
    }
}

@Composable
private fun SummarySection(summary: PermissionSummary) {
    LazyRow(
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp)
    ) {
        item {
            SummaryCard(
                title = "Toplam uygulama",
                value = summary.totalApps,
                accentColor = MaterialTheme.colorScheme.primary
            )
        }
        item {
            SummaryCard(
                title = "Kamera izni",
                value = summary.cameraApps,
                accentColor = Color(0xFF315D7C)
            )
        }
        item {
            SummaryCard(
                title = "Mikrofon izni",
                value = summary.microphoneApps,
                accentColor = Color(0xFF7A4D12)
            )
        }
        item {
            SummaryCard(
                title = "Konum izni",
                value = summary.locationApps,
                accentColor = Color(0xFF2C6F5D)
            )
        }
        item {
            SummaryCard(
                title = "Yüksek risk",
                value = summary.highRiskApps,
                accentColor = MaterialTheme.colorScheme.error
            )
        }
    }
}

@Composable
private fun FilterSection(
    selectedFilter: AppFilter,
    onFilterSelected: (AppFilter) -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .horizontalScroll(rememberScrollState())
            .padding(horizontal = 20.dp, vertical = 10.dp)
    ) {
        AppFilter.entries.forEach { filter ->
            val selected = filter == selectedFilter
            AssistChip(
                onClick = { onFilterSelected(filter) },
                label = {
                    Text(
                        text = filter.label,
                        fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Normal
                    )
                },
                colors = AssistChipDefaults.assistChipColors(
                    containerColor = if (selected) {
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.12f)
                    } else {
                        MaterialTheme.colorScheme.surface
                    },
                    labelColor = if (selected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                ),
                border = AssistChipDefaults.assistChipBorder(
                    enabled = true,
                    borderColor = if (selected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.34f)
                    }
                )
            )
        }
    }
}

@Composable
private fun AppListItem(
    app: AppPermissionInfo,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(14.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(56.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(MaterialTheme.colorScheme.background)
            ) {
                AppIconImage(
                    icon = app.icon,
                    contentDescription = app.appName,
                    modifier = Modifier.size(42.dp)
                )
            }
            Column(
                verticalArrangement = Arrangement.spacedBy(5.dp),
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 14.dp)
            ) {
                Text(
                    text = app.appName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.SemiBold,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "${app.sensitivePermissionCount} verilmiş hassas",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.error,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
            RiskBadge(riskLevel = app.riskLevel)
        }
    }
}

private fun List<AppPermissionInfo>.toSummary(): PermissionSummary {
    return PermissionSummary(
        totalApps = size,
        cameraApps = count { app -> app.hasGrantedCategory(PermissionCategory.Camera) },
        microphoneApps = count { app -> app.hasGrantedCategory(PermissionCategory.Microphone) },
        locationApps = count { app -> app.hasGrantedCategory(PermissionCategory.Location) },
        highRiskApps = count { app -> app.riskLevel == RiskLevel.High }
    )
}

private fun List<AppPermissionInfo>.applyFilter(filter: AppFilter): List<AppPermissionInfo> {
    return when (filter) {
        AppFilter.All -> this
        AppFilter.Camera -> filter { app -> app.hasGrantedCategory(PermissionCategory.Camera) }
        AppFilter.Microphone -> filter { app -> app.hasGrantedCategory(PermissionCategory.Microphone) }
        AppFilter.Location -> filter { app -> app.hasGrantedCategory(PermissionCategory.Location) }
        AppFilter.HighRisk -> filter { app -> app.riskLevel == RiskLevel.High }
    }
}
