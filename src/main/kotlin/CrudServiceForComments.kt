package ru.netology

interface CrudServiceForComments<E> {
    fun add(entity: E): Long
    fun delete(id: Long)
    fun edit(entity: E)
    fun read(postId: Int): List<E>
    fun getById(commentId: Long, postId: Int): E?
    fun restore(commentId: Long, postId: Int)
}