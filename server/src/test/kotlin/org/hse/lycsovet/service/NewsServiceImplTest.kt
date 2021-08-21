package org.hse.lycsovet.service

import org.hse.lycsovet.*
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito.*
import java.util.*

internal class NewsServiceImplTest {

    private val userService = mock(UserServiceImpl::class.java)
    private val newsCrudRepository = mock(NewsCrudRepository::class.java)
    private val newsService = NewsServiceImpl(userService, newsCrudRepository)

    @Test
    fun newArticleSuccess() {
        val dto = createArticleDto(
            title = "Test",
            description = "Test",
            text = "Test",
            publish = true
        )

        `when`(userService.validate(anyString())).thenReturn(true)
        `when`(userService.checkRoleLevel(anyString(), eq(2), eq(4))).thenReturn(true)

        assertDoesNotThrow {
            newsService.publish("", dto)
        }

        val articleCaptor = ArgumentCaptor.forClass(Article::class.java)
        verify(newsCrudRepository, times(1)).save(articleCaptor.capture())
        val capturedArticles = articleCaptor.allValues
        val article = capturedArticles[0]

        assertEquals(dto.title, article.title)
        assertEquals(dto.description, article.description)
        assertEquals(dto.text, article.text)
        assertEquals(dto.publish, article.published)
    }

    @Test
    fun newArticleFailDueToWrongToken() {
        `when`(userService.validate(anyString())).thenReturn(false)

        assertThrows(ForbiddenException::class.java) {
            newsService.publish("", createArticleDto())
        }
        verify(newsCrudRepository, times(0)).save(any(Article::class.java))
    }

    @Test
    fun newArticleFailDueToLackOfAccess() {
        `when`(userService.validate(anyString())).thenReturn(true)
        `when`(userService.checkRoleLevel(anyString(), eq(2), eq(4))).thenReturn(false)

        assertThrows(ForbiddenException::class.java) {
            newsService.publish("", createArticleDto())
        }
        verify(newsCrudRepository, times(0)).save(any(Article::class.java))
    }

    @Test
    fun publishSuccess() {
        val article = createArticle(published = false)

        `when`(userService.validate(anyString())).thenReturn(true)
        `when`(newsCrudRepository.findById(anyLong())).thenReturn(Optional.of(article))

        assertDoesNotThrow {
            newsService.publish("", 1)
        }
        assertEquals(true, article.published)
        verify(newsCrudRepository, times(1)).save(any(Article::class.java))
    }

    @Test
    fun publishFailDueToPublishedArticle() {
        `when`(userService.validate(anyString())).thenReturn(true)
        `when`(newsCrudRepository.findById(anyLong())).thenReturn(Optional.of(createArticle(published = true)))

        assertThrows(BadRequestException::class.java) {
            newsService.publish("", 1)
        }
        verify(newsCrudRepository, times(0)).save(any(Article::class.java))
    }

    @Test
    fun publishFailDueToMissingArticle() {
        `when`(userService.validate(anyString())).thenReturn(true)
        `when`(newsCrudRepository.findById(anyLong())).thenReturn(Optional.empty())

        assertThrows(NotFoundException::class.java) {
            newsService.publish("", 1)
        }
        verify(newsCrudRepository, times(0)).save(any(Article::class.java))
    }

    @Test
    fun publishFailDueToWrongToken() {
        `when`(userService.validate(anyString())).thenReturn(false)

        assertThrows(ForbiddenException::class.java) {
            newsService.publish("", 1)
        }
        verify(newsCrudRepository, times(0)).save(any(Article::class.java))
    }

    @Test
    fun editSuccess() {
        val dto = createArticleDto(
            id = 1,
            title = "New title",
            description = "New description",
            text = "New text",
            publish = true
        )
        val article = createArticle(
            id = 1,
            title = "Actual title",
            description = "Actual description",
            text = "Actual text",
            published = true
        )

        `when`(userService.validate(anyString())).thenReturn(true)
        `when`(userService.checkRoleLevel(anyString(), eq(2), eq(4))).thenReturn(true)
        `when`(newsCrudRepository.findById(anyLong())).thenReturn(Optional.of(article))

        assertDoesNotThrow {
            newsService.edit("", dto)
        }
        assertEquals(dto.title, article.title)
        assertEquals(dto.description, article.description)
        assertEquals(dto.text, article.text)
        verify(newsCrudRepository, times(1)).save(any(Article::class.java))
    }

    @Test
    fun editFailDueToWrongDTO() {
        `when`(userService.validate(anyString())).thenReturn(true)
        `when`(userService.checkRoleLevel(anyString(), eq(2), eq(4))).thenReturn(true)

        assertThrows(BadRequestException::class.java) {
            newsService.edit("", createArticleDto())
        }
        verify(newsCrudRepository, times(0)).save(any(Article::class.java))
    }

    @Test
    fun editFailDueToMissedArticle() {
        `when`(userService.validate(anyString())).thenReturn(true)
        `when`(userService.checkRoleLevel(anyString(), eq(2), eq(4))).thenReturn(true)
        `when`(newsCrudRepository.findById(anyLong())).thenReturn(Optional.empty())

        assertThrows(NotFoundException::class.java) {
            newsService.edit("", createArticleDto(id = 1))
        }
        verify(newsCrudRepository, times(0)).save(any(Article::class.java))
    }

    @Test
    fun editFailDueToWrongToken() {
        `when`(userService.validate(anyString())).thenReturn(false)

        assertThrows(ForbiddenException::class.java) {
            newsService.edit("", createArticleDto())
        }
        verify(newsCrudRepository, times(0)).save(any(Article::class.java))
    }

    @Test
    fun editFailDueToLackOfAccess() {
        `when`(userService.validate(anyString())).thenReturn(true)
        `when`(userService.checkRoleLevel(anyString(), eq(2), eq(4))).thenReturn(false)

        assertThrows(ForbiddenException::class.java) {
            newsService.edit("", createArticleDto())
        }
        verify(newsCrudRepository, times(0)).save(any(Article::class.java))
    }

    @Test
    fun deleteSuccess() {
        `when`(userService.validate(anyString())).thenReturn(true)
        `when`(userService.checkRoleLevel(anyString(), eq(2), eq(4))).thenReturn(true)
        `when`(newsCrudRepository.findById(anyLong())).thenReturn(Optional.of(createArticle()))

        assertDoesNotThrow {
            newsService.delete("", 1)
        }
        verify(newsCrudRepository, times(1)).delete(any(Article::class.java))
    }

    @Test
    fun deleteFailDueToMissingArticle() {
        `when`(userService.validate(anyString())).thenReturn(true)
        `when`(userService.checkRoleLevel(anyString(), eq(2), eq(4))).thenReturn(true)
        `when`(newsCrudRepository.findById(anyLong())).thenReturn(Optional.empty())

        assertThrows(NotFoundException::class.java) {
            newsService.delete("", 1)
        }
        verify(newsCrudRepository, times(0)).delete(any(Article::class.java))
    }

    @Test
    fun deleteFailDueToLackOfAccess() {
        `when`(userService.validate(anyString())).thenReturn(true)
        `when`(userService.checkRoleLevel(anyString(), eq(2), eq(4))).thenReturn(false)

        assertThrows(ForbiddenException::class.java) {
            newsService.delete("", 1)
        }
        verify(newsCrudRepository, times(0)).delete(any(Article::class.java))
    }

    @Test
    fun deleteFailDueToWrongToken() {
        `when`(userService.validate(anyString())).thenReturn(false)

        assertThrows(ForbiddenException::class.java) {
            newsService.delete("", 1)
        }
        verify(newsCrudRepository, times(0)).delete(any(Article::class.java))
    }

    private fun createArticle(
        id: Long? = 1,
        title: String = "",
        description: String = "",
        text: String = "",
        published: Boolean = false
    ) = Article(id, title, description, text, published)

    private fun createArticleDto(
        id: Long? = null,
        title: String = "",
        description: String = "",
        text: String = "",
        publish: Boolean = false
    ) = ArticleDTO(id, title, description, text, publish)
}