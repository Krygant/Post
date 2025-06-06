
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import ru.netology.WallService
import ru.netology.Comments
import ru.netology.Post

class WallServiceTest {

    @Before
    fun clearBeforeAddTest() {
        WallService.clear()
    }

    @Test
    fun addPost() {
        val comments = Comments(0, true, true, true, true)
        var post = Post(1, 1, 1, 1, 6, "Hello!", 1, 1, true, comments)
        val checkPost: Post = WallService.add(post) // Добавляем пост

       assertTrue(checkPost.id == 1) // Проверяем, что текст успешно добавился
    }

    @Test
    fun updatePost() {
        val comments = Comments(0, true, true, true, true)
        var post = Post(1, 1, 1, 1, 6, "Hello!", 1, 1, true, comments)
        WallService.add(post) // Добавляем пост
        post = Post(1, 1, 1, 1, 6, "New text", 1, 1, true, comments)
        val result = WallService.update(post)

        assertTrue(result) // Проверяем, что текст обновился
    }

    @Test
    fun nonUpdatePost() {
        val comments = Comments(0, true, true, true, true)
        var post = Post(1, 1, 1, 1, 6, "Hello!", 1, 1, true, comments)
        WallService.add(post) // Добавляем пост
        post = Post(4, 1, 1, 1, 6, "New text", 1, 1, true, comments)
        val result = WallService.update(post)

        assertFalse(result) // Проверяем, что текст не обновился
    }
}
