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
    var comment: Comment,      // Информация о комментариях к записи, объект с полями
    var arrayAttachments: List<Attachments>//поле для хранения вложений

)

class PostNotFoundException(message: String): RuntimeException(message)

object WallService {
    private var nextId = 1 // Следующий уникальный идентификатор
    private var posts = mutableListOf<Post>() // Используем MutableList для удобства работы
    private var comments = mutableListOf<Comment>()

    /**
     * Метод добавляет комментарий к существующему посту
     */
    fun createComment(postId: Int, comment: Comment): Comment {
        // Поиск поста по идентификатору
        val existingPost = findPostById(postId)

        if (existingPost != null) { // Пост найден
            comments.add(comment)
            return comment
        } else {
            throw PostNotFoundException("Пост с номером $postId не существует")
        }
    }

    /**
     * Метод создает новую запись на стене
     */
    fun add(post: Post): Post {
        val newPost = post.copy(id = nextId++) // Присваиваем уникальный id и увеличиваем счетчик
        posts.add(newPost)                     // Добавляем новый пост в коллекцию
        return newPost                         // Возвращаем созданный пост
    }

    /**
     * Метод обновляет существующий пост
     */
    fun update(post: Post): Boolean {
        val index = posts.indexOfFirst { it.id == post.id } // Находим индекс по идентификатору
        if (index >= 0) {                                  // Индекс найден
            posts[index] = post                             // Обновляем запись
            return true                                     // Возврат успешности операции
        }
        return false                                        // Пост не найден
    }

    /**
     * Вспомогательная функция для поиска поста по id
     */
    private fun findPostById(id: Int): Post? =
        posts.find { it.id == id } // Поиск поста по идентификатору

    /**
     * Метод выводит список всех постов
     */
    fun printPosts() {
        for (post in posts) {
            println(post)
        }
    }

    /**
     * Очистка хранилища постов и комментариев
     */
    fun clear() {
        posts.clear()
        comments.clear()
        nextId = 1
    }
}

fun main() {
    var comment = Comment(1,1, true, true, true, true)

    // Создаем вложения
    var photoAttachment = PhotoAttachment(Photo(1, 1, "photo130.jpg", "photo604.jpg"))
    var videoAttachment = VideoAttachment(Video(1, 1, "My Video", 120))
    var attachmentsList = listOf(photoAttachment, videoAttachment)
    var post = Post(0, 1, 1, 1, 6, "Hello!", 1, 1, true, comment, attachmentsList)
    WallService.add(post)

    // Новый пост с обновлениями
    comment = Comment(2, 1,true, true, true, true)
    photoAttachment = PhotoAttachment(Photo(1, 1, "photo111.jpg", "photo4444.jpg"))
    videoAttachment = VideoAttachment(Video(1, 1, "Updated Video", 120))
    attachmentsList = listOf(photoAttachment, videoAttachment)
    post = Post(0, 1, 1, 1, 6, "Updated bye!", 1, 1, true, comment, attachmentsList)

    val addedPost = WallService.add(post)
    WallService.printPosts() //вывожу на экран все добавленные посты

    println("Added Post ID: ${addedPost.id}") // Печатаем добавленный пост

    // Попробуем обновить наш пост
    var updatedPost = Post(addedPost.id, 1, 1, 1, 6, "Updated text with attachments!", 1, 1, true, comment, attachmentsList)
    var updateResult = WallService.update(updatedPost)
    println("Update successful: $updateResult")

    WallService.printPosts() // Проверяем обновленные посты

    println(WallService.createComment(1, comment))
    println(WallService.createComment(45, comment))

}