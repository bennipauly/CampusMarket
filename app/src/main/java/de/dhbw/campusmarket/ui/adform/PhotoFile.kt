package de.dhbw.campusmarket.ui.adform

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.io.IOException

// Creates an empty file for a new photo in the internal storage (filesDir/photos)
// and returns a content:// Uri for it. The camera app writes the photo into this file.
fun createPhotoUri(context: Context): Uri {
    return fileProviderUri(context, newPhotoFile(context))
}

// Copies a photo picked in the gallery into the app's own photo folder and returns the Uri of the copy.
// The photo picker only grants temporary access to its Uri. Without the copy the photo would be
// gone after an app restart, or as soon as it is deleted from the gallery.
// Reading and writing files can take a moment, so this runs on Dispatchers.IO and not on the main thread.
suspend fun copyPhotoToAppStorage(context: Context, source: Uri): Uri = withContext(Dispatchers.IO) {
    val file = newPhotoFile(context)
    val input = context.contentResolver.openInputStream(source)
        ?: throw IOException("The picked photo cannot be opened")
    // use {} closes both streams, also if copying fails halfway.
    input.use { photoIn ->
        file.outputStream().use { photoOut -> photoIn.copyTo(photoOut) }
    }
    fileProviderUri(context, file)
}

// The name contains the time, so every photo gets its own file.
private fun newPhotoFile(context: Context): File {
    val folder = File(context.filesDir, "photos")
    folder.mkdirs()
    return File(folder, "photo_${System.currentTimeMillis()}.jpg")
}

private fun fileProviderUri(context: Context, file: File): Uri {
    // Must match android:authorities in the manifest.
    return FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
}
