package com.utesocial.android.core.presentation.util

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.ComponentActivity
import androidx.activity.result.contract.ActivityResultContract
import com.utesocial.android.core.data.util.Debug
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okio.BufferedSink
import okio.Okio
import okio.Source
import okio.source
import java.io.InputStream

class FileRequestBody(
    private val inputStream: InputStream,
    val type: String?
) : RequestBody() {
    override fun contentType(): MediaType? {
        return type?.toMediaTypeOrNull()
    }

    override fun contentLength(): Long {
        return inputStream.available().toLong()
    }

    override fun writeTo(sink: BufferedSink) {
        var source : Source? = null
        try {
            source = inputStream.source()
            sink.writeAll(source)
        } catch (e : Exception) {
            Debug.log("FileRequestBody", "writeTo:exception:${e.message}")
        } finally {
            source?.close()
        }
    }

}