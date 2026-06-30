package com.izinlerim.app.data

import com.izinlerim.app.model.PermissionGrantStatus
import com.izinlerim.app.model.PermissionCategory

object PermissionClassifier {
    // Android izin adlarını kullanıcıya anlamlı hassas izin gruplarına eşler.
    fun categoryFor(permissionName: String): PermissionCategory {
        return when (permissionName) {
            "android.permission.CAMERA" -> PermissionCategory.Camera

            "android.permission.RECORD_AUDIO" -> PermissionCategory.Microphone

            "android.permission.ACCESS_FINE_LOCATION",
            "android.permission.ACCESS_COARSE_LOCATION",
            "android.permission.ACCESS_BACKGROUND_LOCATION" -> PermissionCategory.Location

            "android.permission.READ_CONTACTS",
            "android.permission.WRITE_CONTACTS",
            "android.permission.GET_ACCOUNTS" -> PermissionCategory.Contacts

            "android.permission.READ_CALENDAR",
            "android.permission.WRITE_CALENDAR" -> PermissionCategory.Calendar

            "android.permission.SEND_SMS",
            "android.permission.READ_SMS",
            "android.permission.RECEIVE_SMS",
            "android.permission.RECEIVE_MMS",
            "android.permission.RECEIVE_WAP_PUSH" -> PermissionCategory.Sms

            "android.permission.CALL_PHONE",
            "android.permission.READ_PHONE_STATE",
            "android.permission.READ_PHONE_NUMBERS",
            "android.permission.ANSWER_PHONE_CALLS",
            "android.permission.ADD_VOICEMAIL",
            "android.permission.USE_SIP",
            "android.permission.READ_CALL_LOG",
            "android.permission.WRITE_CALL_LOG",
            "android.permission.PROCESS_OUTGOING_CALLS" -> PermissionCategory.Phone

            "android.permission.READ_EXTERNAL_STORAGE",
            "android.permission.WRITE_EXTERNAL_STORAGE",
            "android.permission.MANAGE_EXTERNAL_STORAGE",
            "android.permission.READ_MEDIA_IMAGES",
            "android.permission.READ_MEDIA_VIDEO",
            "android.permission.READ_MEDIA_VISUAL_USER_SELECTED" -> PermissionCategory.Storage

            else -> PermissionCategory.Other
        }
    }

    fun descriptionFor(
        permissionName: String,
        grantStatus: PermissionGrantStatus
    ): String {
        val grantedText = grantedDescriptionFor(permissionName)
        val deniedText = deniedDescriptionFor(permissionName)
        val systemText = systemDescriptionFor(permissionName)

        return when (grantStatus) {
            PermissionGrantStatus.Granted -> grantedText
            PermissionGrantStatus.Denied -> deniedText
            PermissionGrantStatus.SystemManaged -> systemText
        }
    }

    private fun grantedDescriptionFor(permissionName: String): String {
        return when (permissionName) {
            "android.permission.CAMERA" -> "Kamera izni verilmiş. Bu uygulama kameranı kullanabilir."
            "android.permission.RECORD_AUDIO" -> "Mikrofon izni verilmiş. Bu uygulama mikrofonuna erişebilir."
            "android.permission.ACCESS_FINE_LOCATION" -> "Hassas konum izni verilmiş. Bu uygulama tam konumunu görebilir."
            "android.permission.ACCESS_COARSE_LOCATION" -> "Yaklaşık konum izni verilmiş. Bu uygulama yaklaşık konumunu görebilir."
            "android.permission.ACCESS_BACKGROUND_LOCATION" -> "Arka plan konum izni verilmiş. Bu uygulama kapalıyken de konumunu kullanabilir."
            "android.permission.READ_CONTACTS" -> "Kişiler izni verilmiş. Bu uygulama kişi listeni okuyabilir."
            "android.permission.WRITE_CONTACTS" -> "Kişileri değiştirme izni verilmiş. Bu uygulama kişi listende değişiklik yapabilir."
            "android.permission.GET_ACCOUNTS" -> "Hesap bilgisi izni verilmiş. Bu uygulama cihazdaki hesapları görebilir."
            "android.permission.READ_CALENDAR" -> "Takvim okuma izni verilmiş. Bu uygulama takvim etkinliklerini okuyabilir."
            "android.permission.WRITE_CALENDAR" -> "Takvim yazma izni verilmiş. Bu uygulama takvimini değiştirebilir."
            "android.permission.SEND_SMS" -> "SMS gönderme izni verilmiş. Bu uygulama SMS gönderebilir."
            "android.permission.READ_SMS" -> "SMS okuma izni verilmiş. Bu uygulama SMS mesajlarını okuyabilir."
            "android.permission.RECEIVE_SMS" -> "SMS alma izni verilmiş. Bu uygulama gelen SMS mesajlarını algılayabilir."
            "android.permission.RECEIVE_MMS" -> "MMS alma izni verilmiş. Bu uygulama gelen MMS mesajlarını algılayabilir."
            "android.permission.RECEIVE_WAP_PUSH" -> "Servis mesajı alma izni verilmiş. Bu uygulama operatör servis mesajlarını alabilir."
            "android.permission.CALL_PHONE" -> "Arama izni verilmiş. Bu uygulama doğrudan telefon araması başlatabilir."
            "android.permission.READ_PHONE_STATE" -> "Telefon durumu izni verilmiş. Bu uygulama hat ve cihaz durumunu okuyabilir."
            "android.permission.READ_PHONE_NUMBERS" -> "Telefon numarası izni verilmiş. Bu uygulama telefon numaranı okuyabilir."
            "android.permission.ANSWER_PHONE_CALLS" -> "Arama yanıtlama izni verilmiş. Bu uygulama gelen aramaları yanıtlayabilir."
            "android.permission.READ_CALL_LOG" -> "Arama geçmişi izni verilmiş. Bu uygulama arama geçmişini okuyabilir."
            "android.permission.WRITE_CALL_LOG" -> "Arama geçmişi değiştirme izni verilmiş. Bu uygulama arama geçmişinde değişiklik yapabilir."
            "android.permission.READ_EXTERNAL_STORAGE" -> "Dosya/medya okuma izni verilmiş. Bu uygulama cihazındaki dosya ve medyaları okuyabilir."
            "android.permission.WRITE_EXTERNAL_STORAGE" -> "Dosya/medya değiştirme izni verilmiş. Bu uygulama dosyalarda değişiklik yapabilir."
            "android.permission.READ_MEDIA_IMAGES" -> "Fotoğraf izni verilmiş. Bu uygulama fotoğraflarını okuyabilir."
            "android.permission.READ_MEDIA_VIDEO" -> "Video izni verilmiş. Bu uygulama videolarını okuyabilir."
            "android.permission.READ_MEDIA_VISUAL_USER_SELECTED" -> "Seçili medya izni verilmiş. Bu uygulama yalnızca seçtiğin fotoğraf ve videolara erişebilir."
            else -> "${readablePermissionName(permissionName)} izni verilmiş."
        }
    }

    private fun deniedDescriptionFor(permissionName: String): String {
        return when (permissionName) {
            "android.permission.CAMERA" -> "Bu uygulama kamera izni istemiş, ancak şu anda izin verilmemiş."
            "android.permission.RECORD_AUDIO" -> "Bu uygulama mikrofon izni istemiş, ancak şu anda izin verilmemiş."
            "android.permission.ACCESS_FINE_LOCATION" -> "Bu uygulama hassas konum izni istemiş, ancak şu anda izin verilmemiş."
            "android.permission.ACCESS_COARSE_LOCATION" -> "Bu uygulama yaklaşık konum izni istemiş, ancak şu anda izin verilmemiş."
            "android.permission.ACCESS_BACKGROUND_LOCATION" -> "Bu uygulama arka plan konum izni istemiş, ancak şu anda izin verilmemiş."
            "android.permission.READ_CONTACTS" -> "Bu uygulama kişi listeni okuma izni istemiş, ancak şu anda izin verilmemiş."
            "android.permission.WRITE_CONTACTS" -> "Bu uygulama kişilerini değiştirme izni istemiş, ancak şu anda izin verilmemiş."
            "android.permission.GET_ACCOUNTS" -> "Bu uygulama hesap bilgisi izni istemiş, ancak şu anda izin verilmemiş."
            "android.permission.SEND_SMS" -> "Bu uygulama SMS gönderme izni istemiş, ancak şu anda izin verilmemiş."
            "android.permission.READ_SMS" -> "Bu uygulama SMS okuma izni istemiş, ancak şu anda izin verilmemiş."
            "android.permission.RECEIVE_SMS" -> "Bu uygulama SMS alma izni istemiş, ancak şu anda izin verilmemiş."
            "android.permission.CALL_PHONE" -> "Bu uygulama telefon araması başlatma izni istemiş, ancak şu anda izin verilmemiş."
            "android.permission.READ_PHONE_STATE" -> "Bu uygulama telefon durumu izni istemiş, ancak şu anda izin verilmemiş."
            "android.permission.READ_CALL_LOG" -> "Bu uygulama arama geçmişi izni istemiş, ancak şu anda izin verilmemiş."
            "android.permission.WRITE_CALL_LOG" -> "Bu uygulama arama geçmişini değiştirme izni istemiş, ancak şu anda izin verilmemiş."
            "android.permission.READ_MEDIA_IMAGES" -> "Bu uygulama fotoğraflarına erişim istemiş, ancak şu anda izin verilmemiş."
            "android.permission.READ_MEDIA_VIDEO" -> "Bu uygulama videolarına erişim istemiş, ancak şu anda izin verilmemiş."
            else -> "Bu uygulama ${readablePermissionName(permissionName)} izni istemiş, ancak şu anda izin verilmemiş."
        }
    }

    private fun systemDescriptionFor(permissionName: String): String {
        return when (permissionName) {
            "android.permission.INTERNET" -> "İnternet erişimi sistem tarafından otomatik yönetilir."
            "android.permission.ACCESS_NETWORK_STATE" -> "Ağ durumunu görme izni sistem tarafından otomatik yönetilir."
            "android.permission.POST_NOTIFICATIONS" -> "Bildirim izni Android tarafından ayrıca yönetilir."
            else -> "Bu teknik izin Android tarafından otomatik yönetilir veya kullanıcı tarafından ayrıca açılıp kapatılamaz."
        }
    }

    private fun readablePermissionName(permissionName: String): String {
        return permissionName
            .substringAfterLast('.')
            .replace('_', ' ')
            .lowercase()
    }
}
