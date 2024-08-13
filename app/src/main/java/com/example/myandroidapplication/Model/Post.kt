package com.example.myandroidapplication.Model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Post(
    @PrimaryKey val id: String = "",
    val userId: String = "",
    val contentType: ContentType = ContentType.TEXT,
    val contentUrl: String? = "",
    val textContent: String? = "",
    val timestamp: Long? = null, // Use Long for timestamp
    val image: ByteArray? = null // Field to store image as ByteArray
) {
    enum class ContentType {
        TEXT,
        IMAGE,
        GIF
    }

    companion object {
        const val USER_ID_KEY = "userId"
        const val CONTENT_TYPE_KEY = "contentType"
        const val CONTENT_URL_KEY = "contentUrl"
        const val TEXT_CONTENT_KEY = "textContent"
        const val TIMESTAMP_KEY = "timestamp"
        const val IMAGE_KEY = "image"

        fun fromJson(json: Map<String, Any?>): Post {
            val id = json["id"] as? String ?: ""
            val userId = json[USER_ID_KEY] as? String ?: ""
            val contentType = (json[CONTENT_TYPE_KEY] as? String)?.let { ContentType.valueOf(it) } ?: ContentType.TEXT
            val contentUrl = json[CONTENT_URL_KEY] as? String
            val textContent = json[TEXT_CONTENT_KEY] as? String
            val timestamp = (json[TIMESTAMP_KEY] as? com.google.firebase.Timestamp)?.seconds?.let { it * 1000 } // Convert to milliseconds
            val image = json[IMAGE_KEY] as? ByteArray

            return Post(id, userId, contentType, contentUrl, textContent, timestamp, image)
        }
    }

    val json: Map<String, Any?>
        get() {
            return hashMapOf(
                "id" to id,
                USER_ID_KEY to userId,
                CONTENT_TYPE_KEY to contentType.name,
                CONTENT_URL_KEY to contentUrl,
                TEXT_CONTENT_KEY to textContent,
                TIMESTAMP_KEY to (timestamp ?: System.currentTimeMillis()), // Use current time in milliseconds
                IMAGE_KEY to image
            )
        }
}
