package org.hse.lycsovet.service

import org.hse.lycsovet.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.mockito.ArgumentCaptor
import org.mockito.Mockito
import org.mockito.Mockito.*
import java.util.*
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith

internal class NewsServiceImplTest {

    private val userService = mock(UserServiceImpl::class.java)
    private val newsCrudRepository = mock(NewsCrudRepository::class.java)
    private val newsService = NewsServiceImpl(userService, newsCrudRepository)

    @Test
    fun newArticleSuccess() {
        val dto = ArticleDTO(
            null,
            "Test",
            "Test",
            "Test",
            true
        )

        `when`(userService.validate("")).thenReturn(true)
        `when`(userService.checkRoleLevel("", 2, 4)).thenReturn(true)

        assertDoesNotThrow {
            newsService.publish("", dto)
        }

        val articleCaptor = ArgumentCaptor.forClass(Article::class.java)
        verify(newsCrudRepository).save(articleCaptor.capture())
        val capturedArticles = articleCaptor.allValues
        val article = capturedArticles[0]

        assertEquals(dto.title, article.title)
        assertEquals(dto.description, article.description)
        assertEquals(dto.text, article.text)
        assertEquals(dto.publish, article.published)
        verify(newsCrudRepository, times(1)).save(any(Article::class.java))
    }

    @Test
    fun newArticleFailDueToWrongToken() {
        val dto = ArticleDTO(
            null,
            "Test",
            "Test",
            "Test",
            true
        )

        `when`(userService.validate("")).thenReturn(false)

        assertFailsWith(ForbiddenException::class) {
            newsService.publish("", dto)
        }
        verify(newsCrudRepository, times(0)).save(any(Article::class.java))
    }

    @Test
    fun newArticleFailDueToLackOfAccess() {
        val dto = ArticleDTO(
            null,
            "Test",
            "Test",
            "Test",
            true
        )

        `when`(userService.validate("")).thenReturn(true)
        `when`(userService.checkRoleLevel("", 2, 4)).thenReturn(false)

        assertFailsWith(ForbiddenException::class) {
            newsService.publish("", dto)
        }
        verify(newsCrudRepository, times(0)).save(any(Article::class.java))
    }

    @Test
    fun publishSuccess() {
        val article = Article(
            1,
            "Test",
            "Test",
            "Test",
            false
        )

        `when`(userService.validate("")).thenReturn(true)
        `when`(newsCrudRepository.findById(1)).thenReturn(Optional.of(article))

        assertDoesNotThrow {
            newsService.publish("", 1)
        }
        assertEquals(true, article.published)
        verify(newsCrudRepository, times(1)).save(any(Article::class.java))
    }

    @Test
    fun publishFailDueToPublishedArticle() {
        val article = Article(
            1,
            "Test",
            "Test",
            "Test",
            true
        )

        `when`(userService.validate("")).thenReturn(true)
        `when`(newsCrudRepository.findById(1)).thenReturn(Optional.of(article))

        assertFailsWith(BadRequestException::class) {
            newsService.publish("", 1)
        }
        verify(newsCrudRepository, times(0)).save(any(Article::class.java))
    }

    @Test
    fun publishFailDueToMissingArticle() {
        `when`(userService.validate("")).thenReturn(true)
        `when`(newsCrudRepository.findById(1)).thenReturn(Optional.empty())

        assertFailsWith(NotFoundException::class) {
            newsService.publish("", 1)
        }
        verify(newsCrudRepository, times(0)).save(any(Article::class.java))
    }

    @Test
    fun publishFailDueToWrongToken() {
        `when`(userService.validate("")).thenReturn(false)

        assertFailsWith(ForbiddenException::class) {
            newsService.publish("", 1)
        }
        verify(newsCrudRepository, times(0)).save(any(Article::class.java))
    }

    @Test
    fun editSuccess() {
        val dto = ArticleDTO(
            1,
            "New title",
            "New description",
            "New text",
            true
        )
        val article = Article(
            1,
            "Actual title",
            "Actual description",
            "Actual text",
            true
        )

        `when`(userService.validate("")).thenReturn(true)
        `when`(userService.checkRoleLevel("", 2, 4)).thenReturn(true)
        `when`(newsCrudRepository.findById(1)).thenReturn(Optional.of(article))

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
        val dto = ArticleDTO(
            null,
            "Test",
            "Test",
            "Test",
            true
        )

        `when`(userService.validate("")).thenReturn(true)
        `when`(userService.checkRoleLevel("", 2, 4)).thenReturn(true)

        assertFailsWith(BadRequestException::class) {
            newsService.edit("", dto)
        }
        verify(newsCrudRepository, times(0)).save(any(Article::class.java))
    }

    @Test
    fun editFailDueToMissedArticle() {
        val dto = ArticleDTO(
            1,
            "Test",
            "Test",
            "Test",
            true
        )

        `when`(userService.validate("")).thenReturn(true)
        `when`(userService.checkRoleLevel("", 2, 4)).thenReturn(true)
        `when`(newsCrudRepository.findById(1)).thenReturn(Optional.empty())

        assertFailsWith(NotFoundException::class) {
            newsService.edit("", dto)
        }
        verify(newsCrudRepository, times(0)).save(any(Article::class.java))
    }

    @Test
    fun editFailDueToWrongToken() {
        val dto = ArticleDTO(
            1,
            "Test",
            "Test",
            "Test",
            true
        )

        `when`(userService.validate("")).thenReturn(false)

        assertFailsWith(ForbiddenException::class) {
            newsService.edit("", dto)
        }
        verify(newsCrudRepository, times(0)).save(any(Article::class.java))
    }

    @Test
    fun editFailDueToLackOfAccess() {
        val dto = ArticleDTO(
            1,
            "Test",
            "Test",
            "Test",
            true
        )

        `when`(userService.validate("")).thenReturn(true)
        `when`(userService.checkRoleLevel("", 2, 4)).thenReturn(false)

        assertFailsWith(ForbiddenException::class) {
            newsService.edit("", dto)
        }
        verify(newsCrudRepository, times(0)).save(any(Article::class.java))
    }

    @Test
    fun deleteSuccess() {
        val article = Article(
            1,
            "Test",
            "Test",
            "Test",
            true
        )

        `when`(userService.validate("")).thenReturn(true)
        `when`(userService.checkRoleLevel("", 2, 4)).thenReturn(true)
        `when`(newsCrudRepository.findById(1)).thenReturn(Optional.of(article))

        assertDoesNotThrow {
            newsService.delete("", 1)
        }
        verify(newsCrudRepository, times(1)).delete(any(Article::class.java))
    }

    @Test
    fun deleteFailDueToMissingArticle() {
        `when`(userService.validate("")).thenReturn(true)
        `when`(userService.checkRoleLevel("", 2, 4)).thenReturn(true)
        `when`(newsCrudRepository.findById(1)).thenReturn(Optional.empty())

        assertFailsWith(NotFoundException::class) {
            newsService.delete("", 1)
        }
        verify(newsCrudRepository, times(0)).delete(any(Article::class.java))
    }

    @Test
    fun deleteFailDueToLackOfAccess() {
        `when`(userService.validate("")).thenReturn(true)
        `when`(userService.checkRoleLevel("", 2, 4)).thenReturn(false)

        assertFailsWith(ForbiddenException::class) {
            newsService.delete("", 1)
        }
        verify(newsCrudRepository, times(0)).delete(any(Article::class.java))
    }

    @Test
    fun deleteFailDueToWrongToken() {
        `when`(userService.validate("")).thenReturn(false)

        assertFailsWith(ForbiddenException::class) {
            newsService.delete("", 1)
        }
        verify(newsCrudRepository, times(0)).delete(any(Article::class.java))
    }
}