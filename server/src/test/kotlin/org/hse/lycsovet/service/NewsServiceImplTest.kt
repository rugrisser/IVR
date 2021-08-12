package org.hse.lycsovet.service

import org.hse.lycsovet.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertDoesNotThrow
import org.mockito.Mockito
import org.mockito.Mockito.mock
import java.util.*
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

        Mockito.`when`(userService.validate("")).thenReturn(true)
        Mockito.`when`(userService.checkRoleLevel("", 2, 4)).thenReturn(true)

        assertDoesNotThrow {
            newsService.publish("", dto)
        }
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

        Mockito.`when`(userService.validate("")).thenReturn(false)

        assertFailsWith(ForbiddenException::class) {
            newsService.publish("", dto)
        }
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

        Mockito.`when`(userService.validate("")).thenReturn(true)
        Mockito.`when`(userService.checkRoleLevel("", 2, 4)).thenReturn(false)

        assertFailsWith(ForbiddenException::class) {
            newsService.publish("", dto)
        }
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

        Mockito.`when`(userService.validate("")).thenReturn(true)
        Mockito.`when`(newsCrudRepository.findById(1)).thenReturn(Optional.of(article))

        assertDoesNotThrow {
            newsService.publish("", 1)
        }
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

        Mockito.`when`(userService.validate("")).thenReturn(true)
        Mockito.`when`(newsCrudRepository.findById(1)).thenReturn(Optional.of(article))

        assertFailsWith(BadRequestException::class) {
            newsService.publish("", 1)
        }
    }

    @Test
    fun publishFailDueToMissingArticle() {
        Mockito.`when`(userService.validate("")).thenReturn(true)
        Mockito.`when`(newsCrudRepository.findById(1)).thenReturn(Optional.empty())

        assertFailsWith(NotFoundException::class) {
            newsService.publish("", 1)
        }
    }

    @Test
    fun publishFailDueToWrongToken() {
        Mockito.`when`(userService.validate("")).thenReturn(false)

        assertFailsWith(ForbiddenException::class) {
            newsService.publish("", 1)
        }
    }

    @Test
    fun editSuccess() {
        val dto = ArticleDTO(
            1,
            "Test",
            "Test",
            "Test",
            true
        )
        val article = Article(
            1,
            "Test",
            "Test",
            "Test",
            true
        )

        Mockito.`when`(userService.validate("")).thenReturn(true)
        Mockito.`when`(userService.checkRoleLevel("", 2, 4)).thenReturn(true)
        Mockito.`when`(newsCrudRepository.findById(1)).thenReturn(Optional.of(article))

        assertDoesNotThrow {
            newsService.edit("", dto)
        }
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

        Mockito.`when`(userService.validate("")).thenReturn(true)
        Mockito.`when`(userService.checkRoleLevel("", 2, 4)).thenReturn(true)

        assertFailsWith(BadRequestException::class) {
            newsService.edit("", dto)
        }
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

        Mockito.`when`(userService.validate("")).thenReturn(true)
        Mockito.`when`(userService.checkRoleLevel("", 2, 4)).thenReturn(true)
        Mockito.`when`(newsCrudRepository.findById(1)).thenReturn(Optional.empty())

        assertFailsWith(NotFoundException::class) {
            newsService.edit("", dto)
        }
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

        Mockito.`when`(userService.validate("")).thenReturn(false)

        assertFailsWith(ForbiddenException::class) {
            newsService.edit("", dto)
        }
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

        Mockito.`when`(userService.validate("")).thenReturn(true)
        Mockito.`when`(userService.checkRoleLevel("", 2, 4)).thenReturn(false)

        assertFailsWith(ForbiddenException::class) {
            newsService.edit("", dto)
        }
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

        Mockito.`when`(userService.validate("")).thenReturn(true)
        Mockito.`when`(userService.checkRoleLevel("", 2, 4)).thenReturn(true)
        Mockito.`when`(newsCrudRepository.findById(1)).thenReturn(Optional.of(article))

        assertDoesNotThrow {
            newsService.delete("", 1)
        }
    }

    @Test
    fun deleteFailDueToMissingArticle() {
        Mockito.`when`(userService.validate("")).thenReturn(true)
        Mockito.`when`(userService.checkRoleLevel("", 2, 4)).thenReturn(true)
        Mockito.`when`(newsCrudRepository.findById(1)).thenReturn(Optional.empty())

        assertFailsWith(NotFoundException::class) {
            newsService.delete("", 1)
        }
    }

    @Test
    fun deleteFailDueToLackOfAccess() {
        Mockito.`when`(userService.validate("")).thenReturn(true)
        Mockito.`when`(userService.checkRoleLevel("", 2, 4)).thenReturn(false)

        assertFailsWith(ForbiddenException::class) {
            newsService.delete("", 1)
        }
    }

    @Test
    fun deleteFailDueToWrongToken() {
        Mockito.`when`(userService.validate("")).thenReturn(false)

        assertFailsWith(ForbiddenException::class) {
            newsService.delete("", 1)
        }
    }
}