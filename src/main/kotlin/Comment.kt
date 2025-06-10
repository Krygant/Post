package ru.netology

// Информация о комментариях к записи, объект с полями
data class Comment(
    val id: Int,
    val count: Int,             // Количество комментариев
    val canPost: Boolean,       // true если текущий пользователь может комментировать запись
    val groupsCanPost: Boolean, // true могут ли сообщества комментировать запись
    val canClose: Boolean,      // true может ли текущий пользователь закрыть комментарии к записи
    val canOpen: Boolean        // true может ли текущий пользователь открыть комментарии к записи
)