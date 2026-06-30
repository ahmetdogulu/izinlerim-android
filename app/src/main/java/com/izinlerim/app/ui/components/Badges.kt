package com.izinlerim.app.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.izinlerim.app.model.PermissionGrantStatus
import com.izinlerim.app.model.RiskLevel

@Composable
fun RiskBadge(
    riskLevel: RiskLevel,
    modifier: Modifier = Modifier
) {
    val (background, foreground) = when (riskLevel) {
        RiskLevel.Low -> Color(0xFFDFF4EA) to Color(0xFF0F6B45)
        RiskLevel.Medium -> Color(0xFFFFEAC2) to Color(0xFF815300)
        RiskLevel.High -> Color(0xFFFFE1DD) to Color(0xFF9F1F16)
    }

    Box(
        modifier = modifier
            .background(background, RoundedCornerShape(8.dp))
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(
            text = "${riskLevel.label} risk",
            color = foreground,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun SensitiveBadge(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(Color(0xFFFFE1DD), RoundedCornerShape(8.dp))
            .padding(horizontal = 9.dp, vertical = 5.dp)
    ) {
        Text(
            text = "Hassas",
            color = Color(0xFF9F1F16),
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun PermissionStatusBadge(
    grantStatus: PermissionGrantStatus,
    modifier: Modifier = Modifier
) {
    val (background, foreground) = when (grantStatus) {
        PermissionGrantStatus.Granted -> Color(0xFFDFF4EA) to Color(0xFF0F6B45)
        PermissionGrantStatus.Denied -> Color(0xFFF0F2F5) to Color(0xFF5C6670)
        PermissionGrantStatus.SystemManaged -> Color(0xFFE2F0F5) to Color(0xFF2D5F73)
    }

    Box(
        modifier = modifier
            .background(background, RoundedCornerShape(8.dp))
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = grantStatus.label,
            color = foreground,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.SemiBold
        )
    }
}
