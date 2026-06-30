package com.izinlerim.app.model

import android.graphics.drawable.Drawable

enum class PermissionCategory(
    val displayName: String,
    val isSensitive: Boolean
) {
    Camera("Kamera", true),
    Microphone("Mikrofon", true),
    Location("Konum", true),
    Contacts("Kişiler", true),
    Calendar("Takvim", false),
    Sms("SMS", true),
    Phone("Telefon", true),
    Storage("Depolama / Fotoğraf", false),
    Other("Diğer izinler", false)
}

enum class RiskLevel(val label: String) {
    Low("Düşük"),
    Medium("Orta"),
    High("Yüksek")
}

enum class AppFilter(val label: String) {
    All("Tüm uygulamalar"),
    Camera("Kamera"),
    Microphone("Mikrofon"),
    Location("Konum"),
    HighRisk("Yüksek risk")
}

enum class AppUsageCategory(val displayName: String) {
    GameEntertainment("Oyun / Eğlence"),
    Social("Sosyal"),
    Productivity("Üretkenlik"),
    News("Haberler"),
    Maps("Harita / Navigasyon"),
    Other("Diğer")
}

enum class PermissionGrantStatus(
    val label: String,
    val userExplanation: String
) {
    Granted(
        label = "İzin verildi",
        userExplanation = "Bu izne şu anda izin verilmiş."
    ),
    Denied(
        label = "İzin verilmedi",
        userExplanation = "Bu izin istenmiş ama şu anda verilmemiş."
    ),
    SystemManaged(
        label = "Sistem izni",
        userExplanation = "Bu izin Android tarafından otomatik yönetilir; genelde ayrı bir kullanıcı onayı gerektirmez."
    )
}

data class RiskAnalysis(
    val riskScore: Int,
    val riskLevel: RiskLevel,
    val sensitivePermissionCount: Int,
    val explanation: String,
    val extraWarning: String?
)

data class PermissionItem(
    val technicalName: String,
    val displayName: String,
    val description: String,
    val grantStatus: PermissionGrantStatus,
    val category: PermissionCategory
) {
    val isSensitive: Boolean = category.isSensitive
    val isGranted: Boolean = grantStatus == PermissionGrantStatus.Granted
}

data class AppPermissionInfo(
    val appName: String,
    val packageName: String,
    val icon: Drawable,
    val appCategory: AppUsageCategory,
    val requestedPermissions: List<PermissionItem>,
    val riskScore: Int,
    val sensitivePermissionCount: Int,
    val riskLevel: RiskLevel,
    val riskExplanation: String,
    val extraWarning: String?
) {
    fun hasCategory(category: PermissionCategory): Boolean {
        return requestedPermissions.any { it.category == category }
    }

    val userVisiblePermissions: List<PermissionItem>
        get() = requestedPermissions.filter { permission ->
            permission.grantStatus != PermissionGrantStatus.SystemManaged
        }

    val hiddenSystemPermissionCount: Int
        get() = requestedPermissions.count { permission ->
            permission.grantStatus == PermissionGrantStatus.SystemManaged
        }

    fun hasGrantedCategory(category: PermissionCategory): Boolean {
        return requestedPermissions.any { permission ->
            permission.category == category && permission.isGranted
        }
    }
}

data class PermissionSummary(
    val totalApps: Int,
    val cameraApps: Int,
    val microphoneApps: Int,
    val locationApps: Int,
    val highRiskApps: Int
)
