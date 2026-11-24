package de.dhbw.campusmarket.ui.adform

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File

// Creates an empty file for a new photo in the internal storage (filesDir/photos)
// and returns a content:// Uri for it. The camera app writes the photo into this file.
// The name contains the time, so every photo gets its own file.
fun createPhotoUri(context: Context): Uri {
    val folder = File(context.filesDir, "photos")
    folder.mkdirs()
    val file = File(folder, "photo_${System.currentTimeMillis()}.jpg")
    // Must match android:authorities in the manifest.
    return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
}
