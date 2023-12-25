package com.utesocial.android.feature_create_post.domain.model

import android.net.Uri
import androidx.recyclerview.widget.DiffUtil

data class MediaReq(
    val uri: Uri? = null,
    val isVideo: Boolean = false
)

data class MediaItem(
    val mediaUrl : MediaUrl,
    val isVideo: Boolean = false
) {
    class DiffCallback : DiffUtil.ItemCallback<MediaItem>() {
        override fun areItemsTheSame(oldItem: MediaItem, newItem: MediaItem) = oldItem == newItem

        override fun areContentsTheSame(oldItem: MediaItem, newItem: MediaItem) = oldItem == newItem

    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as MediaItem

        if (mediaUrl != other.mediaUrl) return false
        return isVideo == other.isVideo
    }

    override fun hashCode(): Int {
        var result = mediaUrl?.hashCode() ?: 0
        result = 31 * result + isVideo.hashCode()
        return result
    }


}

sealed class MediaUrl {
    data class LocalMedia(val uri: Uri? = null) : MediaUrl() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            if (!super.equals(other)) return false

            other as LocalMedia

            return uri == other.uri
        }

        override fun hashCode(): Int {
            var result = super.hashCode()
            result = 31 * result + (uri?.hashCode() ?: 0)
            return result
        }
    }
    data class RemoteMedia(val url: String?) : MediaUrl() {
        override fun equals(other: Any?): Boolean {
            if (this === other) return true
            if (javaClass != other?.javaClass) return false
            if (!super.equals(other)) return false

            other as RemoteMedia

            return url == other.url
        }

        override fun hashCode(): Int {
            var result = super.hashCode()
            result = 31 * result + (url?.hashCode() ?: 0)
            return result
        }
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        return javaClass == other?.javaClass
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }


}