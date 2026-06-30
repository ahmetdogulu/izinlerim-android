package com.izinlerim.app.data

import com.izinlerim.app.model.AppUsageCategory
import com.izinlerim.app.model.PermissionCategory
import com.izinlerim.app.model.PermissionItem
import com.izinlerim.app.model.RiskAnalysis
import com.izinlerim.app.model.RiskLevel

class PermissionRiskAnalyzer {
    private val sensitiveCategories = setOf(
        PermissionCategory.Camera,
        PermissionCategory.Microphone,
        PermissionCategory.Location,
        PermissionCategory.Contacts,
        PermissionCategory.Sms,
        PermissionCategory.Phone
    )

    fun analyze(
        appCategory: AppUsageCategory,
        permissions: List<PermissionItem>
    ): RiskAnalysis {
        val sensitivePermissions = permissions.filter { permission ->
            permission.category in sensitiveCategories && permission.isGranted
        }
        val sensitiveCount = sensitivePermissions.size
        val riskLevel = when {
            sensitiveCount >= 4 -> RiskLevel.High
            sensitiveCount >= 2 -> RiskLevel.Medium
            else -> RiskLevel.Low
        }

        return RiskAnalysis(
            riskScore = sensitiveCount,
            riskLevel = riskLevel,
            sensitivePermissionCount = sensitiveCount,
            explanation = explanationFor(riskLevel, sensitiveCount),
            extraWarning = extraWarningFor(appCategory, sensitivePermissions)
        )
    }

    private fun explanationFor(
        riskLevel: RiskLevel,
        sensitiveCount: Int
    ): String {
        return when (riskLevel) {
            RiskLevel.Low -> {
                if (sensitiveCount == 0) {
                    "Bu uygulamaya şu anda kamera, mikrofon, konum, kişiler, SMS veya telefon erişimi verilmemiş."
                } else {
                    "Bu uygulamaya sınırlı sayıda hassas izin verilmiş. İzinlerin kullanım amacını yine de kontrol edin."
                }
            }
            RiskLevel.Medium -> {
                "Bu uygulamaya birden fazla hassas izin verilmiş. İzinlerin uygulamanın temel işleviyle uyumlu olduğundan emin olun."
            }
            RiskLevel.High -> {
                "Bu uygulamaya çok sayıda hassas izin verilmiş. Kişisel verileriniz açısından dikkatli incelemeniz önerilir."
            }
        }
    }

    private fun extraWarningFor(
        appCategory: AppUsageCategory,
        sensitivePermissions: List<PermissionItem>
    ): String? {
        if (appCategory != AppUsageCategory.GameEntertainment) return null

        val suspiciousCategories = sensitivePermissions
            .map { permission -> permission.category }
            .filter { category ->
                category == PermissionCategory.Microphone ||
                    category == PermissionCategory.Location ||
                    category == PermissionCategory.Contacts
            }
            .distinct()

        if (suspiciousCategories.isEmpty()) return null

        val permissionNames = suspiciousCategories.joinToString(", ") { category ->
            category.displayName.lowercase()
        }
        return "Oyun/eğlence kategorisindeki bu uygulamaya $permissionNames izni verilmiş. Bu izinleri gerçekten gerekli mi diye kontrol edin."
    }
}
