package ai.juntunen.welcomeback.services

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream

/**
 * Copies a picked photo (typically from `PickVisualMedia`) into the app's
 * internal files dir and returns a stable `photo:<filename>` URL string the
 * UI layer can re-load later. Mirrors the iOS PersistenceService convention.
 */
object PhotoStorage {

    private const val PHOTOS_DIR = "photos"

    /** Copies the content at [uri] into internal storage under [id], returns
     *  a `photo:<filename>` token. */
    fun savePhoto(context: Context, uri: Uri, id: String): String? {
        val photosDir = File(context.filesDir, PHOTOS_DIR).apply { mkdirs() }
        val filename = "$id.jpg"
        val dest = File(photosDir, filename)
        return try {
            context.contentResolver.openInputStream(uri)?.use { input ->
                FileOutputStream(dest).use { output -> input.copyTo(output) }
            }
            "photo:$filename"
        } catch (e: Throwable) {
            null
        }
    }

    /** Resolves a `photo:<filename>` token back into a [File] for Coil to load. */
    fun fileFor(context: Context, token: String): File? {
        if (!token.startsWith("photo:")) return null
        val filename = token.removePrefix("photo:")
        val file = File(File(context.filesDir, PHOTOS_DIR), filename)
        return if (file.exists()) file else null
    }
}
