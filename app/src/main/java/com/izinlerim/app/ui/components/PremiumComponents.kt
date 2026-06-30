package com.izinlerim.app.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.izinlerim.app.model.AppFilter
import com.izinlerim.app.model.AppPermissionInfo
import com.izinlerim.app.model.PermissionCategory
import com.izinlerim.app.model.PermissionItem

@Composable
fun AppHeader(modifier: Modifier = Modifier) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        ShieldMark()
        Column(verticalArrangement = Arrangement.spacedBy(5.dp)) {
            Text(
                text = "İzinlerim",
                style = MaterialTheme.typography.headlineLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "Yüklü uygulamaların erişim izinlerini sade ve anlaşılır şekilde inceleyin.",
                style = MaterialTheme.typography.bodyLarge,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.68f)
            )
        }
    }
}

@Composable
fun SummaryStatCard(
    value: Int,
    title: String,
    accentColor: Color,
    shortLabel: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.width(156.dp),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(16.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(32.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(accentColor.copy(alpha = 0.12f))
            ) {
                Text(
                    text = shortLabel,
                    style = MaterialTheme.typography.labelMedium,
                    color = accentColor,
                    fontWeight = FontWeight.Bold
                )
            }
            Text(
                text = value.toString(),
                style = MaterialTheme.typography.headlineSmall,
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.66f),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}

@Composable
fun FilterChipRow(
    selectedFilter: AppFilter,
    onFilterSelected: (AppFilter) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.horizontalScroll(rememberScrollState())
    ) {
        AppFilter.entries.forEach { filter ->
            val selected = filter == selectedFilter
            val background by animateColorAsState(
                targetValue = if (selected) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.surface
                },
                label = "filterBackground"
            )
            val foreground by animateColorAsState(
                targetValue = if (selected) {
                    MaterialTheme.colorScheme.onPrimary
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.78f)
                },
                label = "filterForeground"
            )

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(8.dp))
                    .background(background)
                    .clickable { onFilterSelected(filter) }
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Text(
                    text = filter.label,
                    style = MaterialTheme.typography.labelMedium,
                    color = foreground
                )
            }
        }
    }
}

@Composable
fun AppPermissionCard(
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
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
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
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = "${app.sensitivePermissionCount} hassas izin",
                    style = MaterialTheme.typography.bodyMedium,
                    color = if (app.sensitivePermissionCount == 0) {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.58f)
                    } else {
                        MaterialTheme.colorScheme.error
                    },
                    fontWeight = FontWeight.SemiBold
                )
            }
            RiskBadge(riskLevel = app.riskLevel)
        }
    }
}

@Composable
fun PermissionCategorySection(
    category: PermissionCategory,
    permissions: List<PermissionItem>,
    modifier: Modifier = Modifier
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = modifier.fillMaxWidth()
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = category.displayName,
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onBackground
            )
            Text(
                text = "${permissions.size} izin",
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.56f)
            )
        }
        Card(
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(vertical = 4.dp)) {
                permissions.forEachIndexed { index, permission ->
                    PermissionRow(
                        permission = permission,
                        showDivider = index != permissions.lastIndex
                    )
                }
            }
        }
    }
}

@Composable
private fun PermissionRow(
    permission: PermissionItem,
    showDivider: Boolean
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            verticalAlignment = Alignment.Top,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(4.dp),
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = permission.displayName,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = permission.description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.66f)
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
        if (showDivider) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 14.dp, end = 14.dp)
                    .background(MaterialTheme.colorScheme.outline.copy(alpha = 0.22f))
                    .height(1.dp)
            )
        }
    }
}

@Composable
private fun ShieldMark(modifier: Modifier = Modifier) {
    val fill = MaterialTheme.colorScheme.primary
    val check = MaterialTheme.colorScheme.onPrimary
    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .size(52.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.primaryContainer)
    ) {
        Canvas(modifier = Modifier.size(30.dp)) {
            val shield = Path().apply {
                moveTo(size.width / 2f, 0f)
                lineTo(size.width, size.height * 0.18f)
                lineTo(size.width * 0.86f, size.height * 0.70f)
                quadraticBezierTo(
                    size.width * 0.70f,
                    size.height * 0.92f,
                    size.width / 2f,
                    size.height
                )
                quadraticBezierTo(
                    size.width * 0.30f,
                    size.height * 0.92f,
                    size.width * 0.14f,
                    size.height * 0.70f
                )
                lineTo(0f, size.height * 0.18f)
                close()
            }
            drawPath(shield, fill)
            drawLine(
                color = check,
                start = Offset(size.width * 0.30f, size.height * 0.52f),
                end = Offset(size.width * 0.45f, size.height * 0.68f),
                strokeWidth = 4.dp.toPx()
            )
            drawLine(
                color = check,
                start = Offset(size.width * 0.45f, size.height * 0.68f),
                end = Offset(size.width * 0.72f, size.height * 0.36f),
                strokeWidth = 4.dp.toPx()
            )
        }
    }
}
