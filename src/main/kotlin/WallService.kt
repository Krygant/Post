package ru.netology

import ru.netology.Attachment.Photo
import ru.netology.Attachment.PhotoAttachment
import ru.netology.Attachment.Video
import ru.netology.Attachment.VideoAttachment
import ru.netology.WallService.findPostById
import java.util.concurrent.atomic.AtomicLong

class PostNotFoundException(message: String): RuntimeException(message)
class PostDeletedException(message: String): RuntimeException(message)

object WallService {
    private val posts = mutableListOf<Post>() // Хранение постов
    private val ids = AtomicLong(1L) // Генератор уникальных идентификаторов
    private var nextId = 1 // Перемещаем сюда объявление nextId



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
    fun findPostById(id: Int): Post? =
        posts.find { ((it.id == id) && (!it.isDeleted)) } // Поиск поста по идентификатору

    /**
     * Метод выводит список всех постов, исключая удалённые посты и комментарии
     */
    fun printPosts() {
        for (post in posts) {
            if (!post.isDeleted) { // Проверяем, что пост не удалён
                println(post) // Используем переопределённый toString()
                println("Комментарии:")
                // Фильтруем комментарии, показывая только неотмеченные как удалённые
                post.commentsMutableList.filter { !it.isDeleted }.forEach(::println)
            }
        }
    }

    /**
    * Метод выводит конкретный пост по номеру id
     */
    fun printPost(id: Int){
        println(posts[id])
    }

    /**
     * Очистка хранилища постов и комментариев
     */
    fun clear() {
        posts.forEach {
            it.isDeleted = true                     // Логически удаляем все посты
            it.commentsMutableList.clear()          // Очищаем комментарии у каждого поста
        }
        nextId = 1
    }

    class PostService: CrudService<Post>{
        /**
         * Метод создает новую запись на стене
         */
        override fun add(entity: Post): Long {
            val newPost = entity.copy(id = ids.getAndIncrement().toInt(), isDeleted = false)
            posts.add(newPost)
            return newPost.id.toLong()
        }

        override fun delete(id: Long) {
            val index = posts.indexOfFirst { it.id == id.toInt() }
            if (index >= 0) {
                val postToDelete = posts[index]
                // Пометить пост как удалённый
                postToDelete.isDeleted = true
                if (postToDelete.commentsMutableList != null) {
                    // Проставить flag 'isDeleted' для всех комментариев поста
                    postToDelete.commentsMutableList.forEach { it.isDeleted = true }
                }
            } else throw PostNotFoundException("Пост с номером ${id.toInt()} не существует")
        }

        override fun edit(entity: Post) {
        val index = posts.indexOfFirst { it.id == entity.id }
            if (index >= 0) {
                posts[index] = entity
            }
        }

        override fun read(): List<Post> {
            return posts.filter { !it.isDeleted }.toList() // Возвращаем только непомеченные как удалённые
        }

        override fun getById(id: Long): Post {
            return posts.firstOrNull { it.id == id.toInt() && !it.isDeleted } ?: throw IllegalArgumentException("Запись с таким id не найдена или удалена.")
        }

        override fun restore(id: Long) {
        val index = posts.indexOfFirst { it.id == id.toInt() }
            if (index >= 0) {
                posts[index].isDeleted = false // Отменяем удаление
            }
        }
    }

    class CommentService: CrudServiceForComments<Comment>{
        /**
         * Метод добавляет комментарий к существующему посту
         */
        override fun add(entity: Comment): Long {
            val parentPostId = entity.parentPostId
            val existingPost = WallService.posts.find { it.id == parentPostId && !it.isDeleted }

            if (existingPost != null) {
                existingPost.commentsMutableList.add(entity) // Добавляем комментарий к списку комментариев выбранного поста
                return entity.id.toLong()
            } else {
                throw PostNotFoundException("Пост с номером $parentPostId не существует")
            }
        }

        override fun delete(id: Long, idParents: Long) {
            // Ищем пост, связанный с указанным ID
            val existingPost = findPostById(idParents.toInt())
            if (existingPost != null) {
                // Пробегаемся по всем комментариям поста и находим нужный
                val commentIndex = existingPost.commentsMutableList.indexOfFirst { it.id == id.toInt() }
                if (commentIndex >= 0) {
                    val targetComment = existingPost.commentsMutableList[commentIndex]
                    if (targetComment.isDeleted) {
                        throw PostDeletedException("Комментарий с номером $id уже удалён")
                    }
                    // Ставим флаг deleted
                    targetComment.isDeleted = true
                } else {
                    throw PostNotFoundException("Комментарий с номером $id не найден")
                }
            } else {
                throw PostNotFoundException("Пост с номером $idParents не существует")
            }
        }

        override fun edit(entity: Comment) {
            val existingPost = findPostById(entity.id)
            if (existingPost != null) {
                val commentIndex = existingPost.commentsMutableList.indexOfFirst { it.id == entity.id }

                // Проверяем, не удалён ли комментарий или его пост
                if (commentIndex < 0 || existingPost.isDeleted || existingPost.commentsMutableList[commentIndex].isDeleted) {
                    throw PostDeletedException("Пост или комментарий к посту ${existingPost.id} удалены")
                }

                // Редактируем комментарий
                existingPost.commentsMutableList[commentIndex] = entity
            } else {
                throw PostNotFoundException("Пост с номером ${entity.id} не существует")
            }
        }

        override fun read(postId: Int): List<Comment> {
            val existingPost = findPostById(postId)
            return if (existingPost != null) {
                existingPost.commentsMutableList.filter { !it.isDeleted }.toList()
            } else emptyList()
        }

        override fun getById(commentId: Long, postId: Int): Comment? {
            val existingPost = findPostById(postId)
            return if (existingPost != null) {
                existingPost.commentsMutableList.firstOrNull { it.id == commentId.toInt() && !it.isDeleted }
            } else null
        }

        override fun restore(commentId: Long, postId: Int) {
            val existingPost = findPostById(postId)
            if (existingPost != null) {
                val commentIndex = existingPost.commentsMutableList.indexOfFirst { it.id == commentId.toInt() }

                // Проверяем существование комментария и отсутствие удаления поста
                if (commentIndex >= 0 && !existingPost.isDeleted) {
                    existingPost.commentsMutableList[commentIndex].isDeleted = false
                } else {
                    throw PostDeletedException("Пост с номером $postId удалён или указанный комментарий не найден")
                }
            } else {
                throw PostNotFoundException("Пост с номером $postId не существует")
            }
        }

    }

}

fun main() {

    val postService = WallService.PostService()

    // Создаем вложения
    var photoAttachment = PhotoAttachment(
        Photo(
        id = 1,
        ownerId = 1,
        photo130 = "photo130.jpg",
        photo604 = "photo604.jpg")
    )

    var videoAttachment = VideoAttachment(
        Video(
            id = 1,
            ownerId = 1,
            title = "My Video",
            duration = 120)
    )

    var attachmentsList = listOf(photoAttachment, videoAttachment)

    var post = Post(
        id = 0,
        ownerId = 1,
        fromID = 1,
        createdBy = 1,
        date = 6,
        text = "Hello!",
        replyOwnerId = 1,
        replyPostId = 1,
        friendsOnly = true,
        arrayAttachments = attachmentsList
    )

    postService.add(post)

    // Новый пост с обновлениями
    photoAttachment = PhotoAttachment(
        Photo(
            id = 1,
            ownerId = 1,
            photo130 = "photo111.jpg",
            photo604 = "photo4444.jpg")
    )

    videoAttachment = VideoAttachment(
        Video(
            id = 1,
            ownerId = 1,
            title = "Updated Video",
            duration = 120)
    )

    attachmentsList = listOf(photoAttachment, videoAttachment)

    post = Post(
        id = 1,
        ownerId = 1,
        fromID = 1,
        createdBy = 1,
        date = 6,
        text = "Updated bye!",
        replyOwnerId = 1,
        replyPostId = 1,
        friendsOnly = true,
        arrayAttachments = attachmentsList
    )

    postService.add(post)

    WallService.printPosts() //вывожу на экран все добавленные посты

    //Попробуем обновить наш пост
    val updatedPost = Post(
        id = 1,
        ownerId = 1,
        fromID = 1,
        createdBy = 1,
        date = 8,
        text = "Updated text with attachments!",
        replyOwnerId = 1,
        replyPostId = 1,
        friendsOnly = true,
        arrayAttachments = attachmentsList
    )

    postService.edit(updatedPost)
    val existingPost = findPostById(updatedPost.id)
    println("Update successful: ${existingPost.toString()}")

    var comment = Comment(
        id = 1,
        parentPostId = 1,
        count = 2,
        canPost = true,
        groupsCanPost = true,
        canClose = true,
        canOpen = true
    )

    var commentService = WallService.CommentService()
    commentService.add(comment)

    comment = Comment(
        id = 2,
        parentPostId = 1,
        count = 2,
        canPost = true,
        groupsCanPost = true,
        canClose = true,
        canOpen = true
    )

    commentService = WallService.CommentService()
    commentService.add(comment)

    comment = Comment(
        id = 1,
        parentPostId = 2,
        count = 4,
        canPost = true,
        groupsCanPost = true,
        canClose = true,
        canOpen = true
    )

    commentService.add(comment)

    println("Вывожу на экран все добавленные посты")
    WallService.printPosts()
    postService.delete(2)

    println("После удаления поста номер 2")

    WallService.printPosts()

    commentService.delete(1, 1)

    println("после удаления комментария id = 1 поста id = 1")

    WallService.printPosts()

    comment = Comment(
        id = 23,
        parentPostId = 1,
        count = 33,
        canPost = true,
        groupsCanPost = true,
        canClose = true,
        canOpen = true
    )

    postService.delete(23)

}