
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import ru.netology.Audio
import ru.netology.AudioAttachment
import ru.netology.WallService
import ru.netology.Comments
import ru.netology.Document
import ru.netology.DocumentAttachment
import ru.netology.Link
import ru.netology.LinkAttachment
import ru.netology.Post
import ru.netology.Video
import ru.netology.VideoAttachment

class WallServiceTest {

    @Before
    fun clearBeforeAddTest() {
        WallService.clear()
    }

    @Test
    fun addPost() {
        val comments = Comments(0, true, true, true, true)
        val documentAttachment = DocumentAttachment(Document(1, 1, "new document", 65))
        val attachmentsList = listOf(documentAttachment)
        val post = Post(0, 1, 1, 1, 6, "Hello!", 1, 1, true, comments, attachmentsList)
        val checkPost: Post = WallService.add(post) // Добавляем пост

        assertEquals(1, checkPost.id) // Проверяем, что идентификатор поста равен 1
        assertEquals("Hello!", checkPost.text) // Проверяем, что текст поста корректен
        assertEquals(attachmentsList, checkPost.arrayAttachments) // Проверяем, что вложения добавлены
    }

    @Test
    fun updatePost() {
        val comments = Comments(0, true, true, true, true)
        val documentLink = LinkAttachment(Link("new link", "new site", "Trump", "Donald John Trump"))
        var attachmentsList: List<LinkAttachment> = listOf(documentLink)
        val post = Post(0, 1, 1, 1, 6, "Hello!", 1, 1, true, comments, attachmentsList)
        WallService.add(post) // Добавляем пост

        val audioAttachment = AudioAttachment(Audio(1, 1, "Ария", "Кровь за кровь"))
        val updatedPost = Post(1, 1, 1, 1, 6, "New text", 1, 1, true, comments, listOf(audioAttachment))
        val result = WallService.update(updatedPost)

        assertTrue(result) // Проверяем, что текст обновился
        assertEquals("New text", WallService.posts.last().text) // Проверяем обновленный текст
        assertTrue(WallService.posts.last().arrayAttachments is List<*>) // Проверяем, что присвоен список вложений
    }

    @Test
    fun nonUpdatePost() {
        val comments = Comments(0, true, true, true, true)
        val documentLink = LinkAttachment(Link("new link", "new site", "Trump", "Donald John Trump"))
        var attachmentsList: List<LinkAttachment> = listOf(documentLink)

        val post = Post(0, 1, 1, 1, 6, "Hello!", 1, 1, true, comments, attachmentsList)
        WallService.add(post) // Добавляем пост

        // Пытаемся обновить пост с id, которого нет
        val nonExistingPost = Post(4, 1, 1, 1, 6, "New text", 1, 1, true, comments, attachmentsList)
        val result = WallService.update(nonExistingPost)

        assertFalse(result) // Проверяем, что текст не обновился
    }
}
