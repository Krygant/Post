package ru.netology

// Информация о комментариях к записи, объект с полями
data class Comment(
    val id: Int = 0,                // id комментария
    val parentPostId: Int = 0,      // id родительского поста
    val count: Int = 0,             // Количество комментариев
    val canPost: Boolean = true,       // true если текущий пользователь может комментировать запись
    val groupsCanPost: Boolean = true, // true могут ли сообщества комментировать запись
    val canClose: Boolean = true,      // true может ли текущий пользователь закрыть комментарии к записи
    val canOpen: Boolean = true,        // true может ли текущий пользователь открыть комментарии к записи
    var isDeleted: Boolean = false
)