package ru.netology
import ru.netology.Attachment.*
import kotlin.collections.mutableListOf

data class Post(
    val id: Int,
    val ownerId: Int?,
    val fromID: Int?,
    val createdBy: Int?,
    val date: Int,
    val text: String,
    val replyOwnerId: Int,
    val replyPostId: Int,
    val friendsOnly: Boolean,
    var arrayAttachments: List<Attachments>,
    val commentsMutableList: MutableList<Comment> = mutableListOf(),
    var isDeleted: Boolean = false
)
{
    override fun toString(): String {
        return """
            |ID: $id
            |Text: $text
            |From: $fromID
            |Date: $date
            """.trimMargin()
    }
}