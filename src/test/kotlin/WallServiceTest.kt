
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import ru.netology.Comment
import ru.netology.Post
import ru.netology.Attachment.*
import ru.netology.WallService

class WallServiceTest {

    @Before
    fun clearBeforeAddTest() {
        WallService.clear()
    }

    @Test
    fun addPost() {
        val comment = Comment(0,1, true, true, true, true)
        val documentAttachment = DocumentAttachment(Document(1, 1, "new document", 65))
        val attachmentsList = listOf(documentAttachment)
        val post = Post(0, 1, 1, 1, 6, "Hello!", 1, 1, true, comment, attachmentsList)
        val checkPost: Post = WallService.add(post) // Добавляем пост

        assertEquals(1, checkPost.id) // Проверяем, что идентификатор поста равен 1
        assertEquals("Hello!", checkPost.text) // Проверяем, что текст поста корректен
        assertEquals(attachmentsList, checkPost.arrayAttachments) // Проверяем, что вложения добавлены
    }

    @Test
    fun updatePost() {
        val comment = Comment(0, 1, true, true, true, true)
        val documentLink = LinkAttachment(Link("new link", "new site", "Trump", "Donald John Trump"))
        var attachmentsList: List<LinkAttachment> = listOf(documentLink)
        val post = Post(0, 1, 1, 1, 6, "Hello!", 1, 1, true, comment, attachmentsList)
        WallService.add(post) // Добавляем пост

        val audioAttachment = AudioAttachment(Audio(1, 1, "Ария", "Кровь за кровь"))
        val updatedPost = Post(1, 1, 1, 1, 6, "New text", 1, 1, true, comment, listOf(audioAttachment))
        val result = WallService.update(updatedPost)

        assertTrue(result) // Проверяем, что текст обновился
    }

    @Test
    fun nonUpdatePost() {
        val comment = Comment(0, 1,true, true, true, true)
        val documentLink = LinkAttachment(Link("new link", "new site", "Trump", "Donald John Trump"))
        var attachmentsList: List<LinkAttachment> = listOf(documentLink)

        val post = Post(0, 1, 1, 1, 6, "Hello!", 1, 1, true, comment, attachmentsList)
        WallService.add(post) // Добавляем пост

        // Пытаемся обновить пост с id, которого нет
        val nonExistingPost = Post(4, 1, 1, 1, 6, "New text", 1, 1, true, comment, attachmentsList)
        val result = WallService.update(nonExistingPost)

        assertFalse(result) // Проверяем, что текст не обновился
    }

    class PostNotFoundException(message: String): RuntimeException(message)

    @Test(expected = ru.netology.PostNotFoundException::class)
    fun shouldThrow() {
        val comment = Comment(1, 1, true, true, true, true)
        val documentLink = LinkAttachment(Link("new link", "new site", "Trump", "Donald John Trump"))
        var attachmentsList: List<LinkAttachment> = listOf(documentLink)

        val post = Post(0, 1, 1, 1, 6, "Hello!", 1, 1, true, comment, attachmentsList)
        WallService.add(post) // Добавляем пост
        var postId = 100 // Несуществующий идентификатор поста
        WallService.createComment(postId, comment)
    }

    @Test
    fun shouldntThrow(){
        val comment = Comment(2, 1,true, true, true, true)
        val documentLink = LinkAttachment(Link("new link", "new site", "Trump", "Donald John Trump"))
        var attachmentsList: List<LinkAttachment> = listOf(documentLink)

        var post = Post(0, 1, 1, 1, 6, "Hello!", 1, 1, true, comment, attachmentsList)
        WallService.add(post) // Добавляем пост
        post = Post(0, 1, 1, 1, 6, "Hello!", 1, 1, true, comment, attachmentsList)
        WallService.add(post) // Добавляем пост

        var result = WallService.createComment(1,comment)

        assertEquals(comment, result)
    }
}
