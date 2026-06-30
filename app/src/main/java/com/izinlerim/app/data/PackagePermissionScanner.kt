package com.izinlerim.app.data

import android.content.Context
import android.content.Intent
import android.content.pm.ApplicationInfo
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.content.pm.PermissionInfo
import android.content.pm.ResolveInfo
import android.os.Build
import com.izinlerim.app.model.AppPermissionInfo
import com.izinlerim.app.model.AppUsageCategory
import com.izinlerim.app.model.PermissionGrantStatus
import com.izinlerim.app.model.PermissionItem
import java.util.Locale

class PackagePermissionScanner(
    context: Context
) {
    private val packageManager = context.packageManager
    private val riskAnalyzer = PermissionRiskAnalyzer()

    fun loadUserApps(): List<AppPermissionInfo> {
        return queryLauncherApps()
            .mapNotNull { resolveInfo -> resolveInfo.activityInfo?.applicationInfo }
            .distinctBy { applicationInfo -> applicationInfo.packageName }
            .filter { applicationInfo -> applicationInfo.isUserInstalledApp() }
            .mapNotNull { applicationInfo -> applicationInfo.toAppPermissionInfo() }
            .sortedBy { app -> app.appName.lowercase(Locale("tr", "TR")) }
    }

    private fun queryLauncherApps(): List<ResolveInfo> {
        val intent = Intent(Intent.ACTION_MAIN).apply {
            addCategory(Intent.CATEGORY_LAUNCHER)
        }

        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.queryIntentActivities(
                intent,
                PackageManager.ResolveInfoFlags.of(PackageManager.MATCH_ALL.toLong())
            )
        } else {
            @Suppress("DEPRECATION")
            packageManager.queryIntentActivities(intent, PackageManager.MATCH_ALL)
        }
    }

    private fun ApplicationInfo.isUserInstalledApp(): Boolean {
        val isSystemApp = flags and ApplicationInfo.FLAG_SYSTEM != 0
        val isUpdatedSystemApp = flags and ApplicationInfo.FLAG_UPDATED_SYSTEM_APP != 0
        return !isSystemApp && !isUpdatedSystemApp
    }

    private fun ApplicationInfo.toAppPermissionInfo(): AppPermissionInfo? {
        val packageInfo = runCatching { getPackageInfoWithPermissions(packageName) }.getOrNull()
            ?: return null
        val permissions = packageInfo.requestedPermissions
            .orEmpty()
            .map { permissionName ->
                val category = PermissionClassifier.categoryFor(permissionName)
                val grantStatus = grantStatusFor(permissionName, packageName)
                PermissionItem(
                    technicalName = permissionName,
                    displayName = displayNameFor(permissionName),
                    description = PermissionClassifier.descriptionFor(permissionName, grantStatus),
                    grantStatus = grantStatus,
                    category = category
                )
            }
            .sortedWith(
                compareBy<PermissionItem> { grantStatusRank(it.grantStatus) }
                    .thenByDescending { it.isSensitive }
                    .thenBy { it.category.displayName }
                    .thenBy { it.displayName }
            )

        val appCategory = usageCategoryFor(category)
        val riskAnalysis = riskAnalyzer.analyze(
            appCategory = appCategory,
            permissions = permissions
        )

        return AppPermissionInfo(
            appName = loadLabel(packageManager).toString(),
            packageName = packageName,
            icon = loadIcon(packageManager),
            appCategory = appCategory,
            requestedPermissions = permissions,
            riskScore = riskAnalysis.riskScore,
            sensitivePermissionCount = riskAnalysis.sensitivePermissionCount,
            riskLevel = riskAnalysis.riskLevel,
            riskExplanation = riskAnalysis.explanation,
            extraWarning = riskAnalysis.extraWarning
        )
    }

    private fun usageCategoryFor(category: Int): AppUsageCategory {
        return when (category) {
            ApplicationInfo.CATEGORY_GAME,
            ApplicationInfo.CATEGORY_AUDIO,
            ApplicationInfo.CATEGORY_VIDEO,
            ApplicationInfo.CATEGORY_IMAGE -> AppUsageCategory.GameEntertainment

            ApplicationInfo.CATEGORY_SOCIAL -> AppUsageCategory.Social
            ApplicationInfo.CATEGORY_PRODUCTIVITY -> AppUsageCategory.Productivity
            ApplicationInfo.CATEGORY_NEWS -> AppUsageCategory.News
            ApplicationInfo.CATEGORY_MAPS -> AppUsageCategory.Maps
            else -> AppUsageCategory.Other
        }
    }

    private fun getPackageInfoWithPermissions(packageName: String): PackageInfo {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            packageManager.getPackageInfo(
                packageName,
                PackageManager.PackageInfoFlags.of(PackageManager.GET_PERMISSIONS.toLong())
            )
        } else {
            @Suppress("DEPRECATION")
            packageManager.getPackageInfo(packageName, PackageManager.GET_PERMISSIONS)
        }
    }

    private fun displayNameFor(permissionName: String): String {
        val label = runCatching {
            getPermissionInfo(permissionName).loadLabel(packageManager).toString()
        }.getOrNull()

        return label
            ?.takeUnless { it.isBlank() }
            ?: permissionName.substringAfterLast('.')
                .replace('_', ' ')
                .lowercase(Locale("tr", "TR"))
                .replaceFirstChar { char -> char.titlecase(Locale("tr", "TR")) }
    }

    private fun grantStatusFor(
        permissionName: String,
        packageName: String
    ): PermissionGrantStatus {
        val permissionInfo = runCatching { getPermissionInfo(permissionName) }.getOrNull()
            ?: return PermissionGrantStatus.SystemManaged

        if (!permissionInfo.isDangerousPermission()) {
            return PermissionGrantStatus.SystemManaged
        }

        return if (packageManager.checkPermission(permissionName, packageName) == PackageManager.PERMISSION_GRANTED) {
            PermissionGrantStatus.Granted
        } else {
            PermissionGrantStatus.Denied
        }
    }

    private fun grantStatusRank(grantStatus: PermissionGrantStatus): Int {
        return when (grantStatus) {
            PermissionGrantStatus.Granted -> 0
            PermissionGrantStatus.Denied -> 1
            PermissionGrantStatus.SystemManaged -> 2
        }
    }

    private fun PermissionInfo.isDangerousPermission(): Boolean {
        @Suppress("DEPRECATION")
        val baseProtection = protectionLevel and PermissionInfo.PROTECTION_MASK_BASE
        return baseProtection == PermissionInfo.PROTECTION_DANGEROUS
    }

    private fun getPermissionInfo(permissionName: String): PermissionInfo {
        @Suppress("DEPRECATION")
        return packageManager.getPermissionInfo(permissionName, 0)
    }
}
