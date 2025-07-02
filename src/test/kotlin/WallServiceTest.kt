package ru.netology.test

import org.junit.*
import org.junit.rules.ExpectedException
import ru.netology.Attachment
import ru.netology.Comment
import ru.netology.Post
import ru.netology.PostNotFoundException
import ru.netology.WallService

class WallServiceTest {

    lateinit var postService: WallService.PostService
    lateinit var commentService: WallService.CommentService

    @Before
    fun setUp() {
        WallService.clear()
        postService = WallService.PostService()
        commentService = WallService.CommentService()
    }

    @Test
    fun addPost() {
        val attachment = Attachment.DocumentAttachment(Attachment.Document(1, 1, "Документ №1", 100))
        val post = Post(
            id = 0,
            ownerId = 1,
            fromID = 1,
            createdBy = 1,
            date = System.currentTimeMillis().toInt(),
            text = "Первый пост",
            replyOwnerId = 1,
            replyPostId = 1,
            friendsOnly = true,
            arrayAttachments = listOf(attachment),
            commentsMutableList = mutableListOf()
        )

        val addedPostId = postService.add(post)
        val foundPost = WallService.findPostById(addedPostId.toInt())

        Assert.assertEquals("Первый пост", foundPost!!.text) // Проверка текста
        Assert.assertEquals(listOf(attachment), foundPost.arrayAttachments) // Проверка документа
    }

    @Test
    fun updatePost() {
        val originalPost = Post(
            id = 1,
            ownerId = 1,
            fromID = 1,
            createdBy = 1,
            date = System.currentTimeMillis().toInt(),
            text = "Старое содержимое",
            replyOwnerId = 1,
            replyPostId = 1,
            friendsOnly = true,
            arrayAttachments = emptyList(),
            commentsMutableList = mutableListOf()
        )

        postService.add(originalPost)

        val updatedPost = originalPost.copy(text = "Новое содержимое")
        val success = WallService.update(updatedPost)

        Assert.assertTrue(success) // Успешное обновление?
        val foundPost = WallService.findPostById(updatedPost.id)
        Assert.assertEquals("Новое содержимое", foundPost!!.text)
    }

    @Test
    fun nonUpdatePost() {
        val originalPost = Post(
            id = 0,
            ownerId = 1,
            fromID = 1,
            createdBy = 1,
            date = System.currentTimeMillis().toInt(),
            text = "Новый пост",
            replyOwnerId = 1,
            replyPostId = 1,
            friendsOnly = true,
            arrayAttachments = emptyList(),
            commentsMutableList = mutableListOf()
        )

        postService.add(originalPost)

        val nonExistentPost = originalPost.copy(id = 999) // Пост с несуществующим ID
        val success = WallService.update(nonExistentPost)

        Assert.assertFalse(success) // Ожидали неудачу обновления несуществующего поста
    }

    @Test
    fun retrieveNonexistentPost() {
        val nonExistPost = WallService.findPostById(999)
        Assert.assertNull(nonExistPost)
    }

    @Test
    fun cleanupData() {
        val post = Post(
            id = 0,
            ownerId = 1,
            fromID = 1,
            createdBy = 1,
            date = System.currentTimeMillis().toInt(),
            text = "Последний пост",
            replyOwnerId = 1,
            replyPostId = 1,
            friendsOnly = true,
            arrayAttachments = emptyList(),
            commentsMutableList = mutableListOf()
        )

        postService.add(post)
        WallService.clear()
        val retrievedPost = WallService.findPostById(post.id)
        Assert.assertNull(retrievedPost)
    }

    @Rule
    @JvmField
    val thrown: ExpectedException = ExpectedException.none()

    @Test
    fun testAddCommentToNonExistingPost() {
        val comment = Comment(
            id = 1,
            parentPostId = 999, // несуществующий идентификатор поста
            count = 2,
            canPost = true,
            groupsCanPost = true,
            canClose = true,
            canOpen = true
        )

        thrown.expect(PostNotFoundException::class.java)
        thrown.expectMessage("Пост с номером 999 не существует")

        commentService.add(comment)
    }
}