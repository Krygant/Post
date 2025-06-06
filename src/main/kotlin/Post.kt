package ru.netology

data class Post(
    val id: Int,                // Идентификатор записи
    val ownerId: Int,           // Идентификатор владельца стены
    val fromID: Int,            // Идентификатор автора записи
    val createdBy: Int,         // Идентификатор администратора, который опубликовал запись
    val date: Int,              // Время публикации
    val text: String,           // Текст записи
    val replyOwnerId: Int,      // Идентификатор владельца записи, в ответ на которую была оставлена текущая
    val replyPostId: Int,       // Идентификатор записи, в ответ на которую была оставлена текущая
    val friendsOnly: Boolean,   // true если запись только для друзей
    val comments: Comments      // Информация о комментариях к записи, объект с полями
)

// Информация о комментариях к записи, объект с полями
data class Comments(
    var count: Int,             // Количество комментариев
    var canPost: Boolean,       // true если текущий пользователь может комментировать запись
    var groupsCanPost: Boolean, // true могут ли сообщества комментировать запись
    var canClose: Boolean,      // true может ли текущий пользователь закрыть комментарии к записи
    var canOpen: Boolean        // true может ли текущий пользователь открыть комментарии к записи
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
                posts[index] = post.copy(comments = p.comments.copy()) // Обновляем пост и сохраняем оригинальный id
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
    var post = Post(0, 1, 1, 1, 6, "Hello!", 1, 1, true, comments)
    WallService.add(post)
    comments = Comments(1, true, true, true, true)
    post = Post(0, 1, 1, 1, 6, "Bye!", 1, 1, true, comments)
    WallService.add(post)
    comments = Comments(1, true, true, true, true)
    post = Post(0, 1, 1, 1, 6, "Some text!", 1, 1, true, comments)
    val addedPost = WallService.add(post)
    WallService.printPosts() //вывожу на экран все добавленные посты

    println(addedPost.id) // Печатаем добавленный пост
    // Попробуем обновить наш пост
    var updatedPost = Post(addedPost.id, 1, 1, 1, 6, "Updated text!", 1, 1, true, comments)
    var updateResult = WallService.update(updatedPost)
    println("Update successful: $updateResult")
    updatedPost = Post(2, 1,1,1,1,"SimpleText", 1, 1, true, comments)
    updateResult = WallService.update(updatedPost)
    println("Update successful: $updateResult")
    println("Updated post: ${WallService.posts.last()}")
    println("Updated post: ${WallService.posts[1]}")
}