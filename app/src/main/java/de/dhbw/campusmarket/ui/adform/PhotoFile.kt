package de.dhbw.campusmarket.ui.adform

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

// Legt eine leere Datei für ein neues Foto im internen Speicher an (filesDir/photos)
// und gibt eine content://-Uri dafür zurück. In diese Datei schreibt die Kamera-App das Foto.
// Der Name enthält die Uhrzeit, damit jedes Foto eine eigene Datei bekommt.
fun createPhotoUri(context: Context): Uri {
    val folder = File(context.filesDir, "photos")
    folder.mkdirs()
    val file = File(folder, "photo_${System.currentTimeMillis()}.jpg")
    // Muss zu android:authorities im Manifest passen.
    return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
}
