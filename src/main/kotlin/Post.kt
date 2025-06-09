package ru.netology
import ru.netology.Attachment.*

data class Post(
    val id: Int,                 // Идентификатор записи
    val ownerId: Int?,           // Идентификатор владельца стены
    val fromID: Int?,            // Идентификатор автора записи
    val createdBy: Int?,         // Идентификатор администратора, который опубликовал запись
    val date: Int,               // Время публикации
    val text: String,            // Текст записи
    val replyOwnerId: Int,       // Идентификатор владельца записи, в ответ на которую была оставлена текущая
    val replyPostId: Int,        // Идентификатор записи, в ответ на которую была оставлена текущая
    val friendsOnly: Boolean,    // true если запись только для друзей
    var comments: Comments,      // Информация о комментариях к записи, объект с полями
    var arrayAttachments: List<Attachments>
)

object WallService {
    var posts = emptyArray<Post>()
    private var nextId = 1 // Переменная для хранения следующего уникального id

    fun add(post: Post): Post {
        val newPost = post.copy(id = nextId) // Копируем пост с новым id
        posts += newPost // Добавляем новый пост в массив
        nextId++ // Увеличиваем следующий id для следующей записи
        return newPost // Возвращаем пост с уникальным id
    }

    fun update(post: Post): Boolean {
        for ((index, p) in posts.withIndex()) {
            if (p.id == post.id) {
                posts[index] = post.copy(comments = p.comments.copy(), arrayAttachments = post.arrayAttachments) // Обновляем пост и сохраняем оригинальный id
                return true
            }
        }
        return false // Если пост не найден, возвращаем false
    }

    fun printPosts(){
        for (post in posts){
            println(post)
        }
    }

    fun clear() {
        posts = emptyArray()
        nextId = 1
    }
}

fun main() {
    var comments = Comments(1, true, true, true, true)

    // Создаем вложения
    var photoAttachment = PhotoAttachment(Photo(1, 1, "photo130.jpg", "photo604.jpg"))
    var videoAttachment = VideoAttachment(Video(1, 1, "My Video", 120))
    var attachmentsList = listOf(photoAttachment, videoAttachment)
    var post = Post(0, 1, 1, 1, 6, "Hello!", 1, 1, true, comments, attachmentsList)
    WallService.add(post)

    // Новый пост с обновлениями
    comments = Comments(1, true, true, true, true)
    photoAttachment = PhotoAttachment(Photo(1, 1, "photo111.jpg", "photo4444.jpg"))
    videoAttachment = VideoAttachment(Video(1, 1, "Updated Video", 120))
    attachmentsList = listOf(photoAttachment, videoAttachment)
    post = Post(0, 1, 1, 1, 6, "Updated bye!", 1, 1, true, comments, attachmentsList)

    val addedPost = WallService.add(post)
    WallService.printPosts() //вывожу на экран все добавленные посты

    println("Added Post ID: ${addedPost.id}") // Печатаем добавленный пост

    // Попробуем обновить наш пост
    var updatedPost = Post(addedPost.id, 1, 1, 1, 6, "Updated text with attachments!", 1, 1, true, comments, attachmentsList)
    var updateResult = WallService.update(updatedPost)
    println("Update successful: $updateResult")

    WallService.printPosts() // Проверяем обновленные посты
}