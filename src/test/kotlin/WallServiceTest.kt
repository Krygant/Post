import org.junit.jupiter.api.*
import org.junit.jupiter.api.Assertions.*
import ru.netology.Attachment
import ru.netology.Comment
import ru.netology.Post
import ru.netology.PostNotFoundException
import ru.netology.WallService

class WallServiceTest {
    val postService = WallService.PostService()
    val commentService = WallService.CommentService()

    @BeforeEach
    fun setUp() {
        WallService.clear()
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

        assertEquals(foundPost?.text, "Первый пост") // Проверяем текст
        assertEquals(foundPost?.arrayAttachments, listOf(attachment)) // Проверяем прикреплённую документацию
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

        assertTrue(success)
        val foundPost = WallService.findPostById(updatedPost.id)
        assertEquals(foundPost?.text, "Новое содержимое")
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

        val nonExistentPost = originalPost.copy(id = 999)
        val success = WallService.update(nonExistentPost)

        assertFalse(success)
    }

    @Nested
    inner class RetrieveTests {
        @Test
        fun retrieveNonexistentPost() {
            val nonExistPost = WallService.findPostById(999)
            assertEquals(null, nonExistPost)
        }
    }

    @Nested
    inner class CleanupTests {
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
            assertEquals(null, retrievedPost)
        }
    }
}