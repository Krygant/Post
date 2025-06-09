package ru.netology

// Информация о комментариях к записи, объект с полями
data class Comments(
    var count: Int,             // Количество комментариев
    var canPost: Boolean,       // true если текущий пользователь может комментировать запись
    var groupsCanPost: Boolean, // true могут ли сообщества комментировать запись
    var canClose: Boolean,      // true может ли текущий пользователь закрыть комментарии к записи
    var canOpen: Boolean        // true может ли текущий пользователь открыть комментарии к записи
)