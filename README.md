# İzinlerim

Android için Kotlin ve Jetpack Compose ile hazırlanmış MVP izin envanteri uygulaması.

## Özellikler

- Telefonda yüklü kullanıcı uygulamalarını listeler.
- Uygulama adı, paket adı, ikon, talep edilen izinler, hassas izin sayısı ve risk seviyesini gösterir.
- Kamera, mikrofon, konum, kişiler, takvim, SMS, telefon ve depolama/fotoğraf izinlerini hassas izin olarak sınıflandırır.
- Ana ekranda toplam uygulama, kamera, mikrofon, konum ve yüksek risk özetlerini gösterir.
- Tüm uygulamalar, kamera, mikrofon, konum ve yüksek risk filtreleri sunar.
- Detay ekranında izinleri kategorilere ayırır ve hassas izinleri uyarı etiketiyle gösterir.
- "Ayarları Aç" butonu ilgili Android uygulama ayar sayfasını açar.

## Teknik Notlar

- Minimum Android sürümü: API 26.
- Üçüncü parti UI kütüphanesi kullanılmadı; arayüz Jetpack Compose ve Material 3 ile kuruldu.
- Android 11+ paket görünürlüğü için manifest içinde launcher uygulamalarını sorgulayan `queries` bildirimi bulunur.
- Liste, kullanıcı tarafından yüklenen launcher uygulamalarına odaklanır. Sistem uygulamaları ve güncellenmiş sistem uygulamaları filtrelenir.

## Çalıştırma

Projeyi Android Studio ile açıp `app` yapılandırmasını çalıştırabilirsiniz. Komut satırında Gradle kuruluysa:

```powershell
gradle :app:assembleDebug
```
