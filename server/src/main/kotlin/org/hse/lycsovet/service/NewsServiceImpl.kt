package org.hse.lycsovet.service

import org.hse.lycsovet.*
import org.springframework.stereotype.Service
import java.util.*

@Service
class NewsServiceImpl(
        private val userService: UserServiceImpl,
        private val newsCrudRepository: NewsCrudRepository
) : NewsService {
    override fun get(id: Long): Article {
        val articleOptional = newsCrudRepository.findById(id)
        if (articleOptional.isEmpty) {
            throw NotFoundException("Article not found")
        }

        return articleOptional.get()
    }

    override fun all(): List<Article> {
        return newsCrudRepository.findAll()
    }

    override fun feed(): List<Article> {
        return newsCrudRepository.findAllByPublished(true)
    }

    override fun publish(token: String, articleDTO: ArticleDTO): Long? {
        if (userService.validate(token)) {
            if (!userService.checkRoleLevel(token, 2, 4)) throw ForbiddenException("You cannot publish articles")
            if (articleDTO.publish == null) {
                articleDTO.publish = false
            }
            val article = Article(null, articleDTO.title, articleDTO.description, articleDTO.text, articleDTO.publish!!)
            newsCrudRepository.save(article)
            return article.id
        }

        throw ForbiddenException("Token is invalid")
    }

    override fun publish(token: String, id: Long) {
        if (userService.validate(token)) {
            val articleOptional = newsCrudRepository.findById(id)
            if (articleOptional.isEmpty) {
                throw NotFoundException("Article not found")
            }

            val article = articleOptional.get()
            if (article.published) {
                throw BadRequestException("Article already published")
            }

            article.published = true
            newsCrudRepository.save(article)
        } else {
            throw ForbiddenException("Token is invalid")
        }
    }

    override fun edit(token: String, articleDTO: ArticleDTO) {
        if (userService.validate(token)) {
            if (!userService.checkRoleLevel(token, 2, 4)) throw ForbiddenException("You cannot edit articles")
            if (articleDTO.id == null) {
                throw BadRequestException("Article ID not given")
            }
            val articleOptional = newsCrudRepository.findById(articleDTO.id)
            if (articleOptional.isEmpty) {
                throw NotFoundException("Article not found")
            }

            val article = articleOptional.get()
            article.title = articleDTO.title
            article.text = articleDTO.text
            article.description = articleDTO.description
            article.updated = Date()

            newsCrudRepository.save(article)
        } else {
            throw ForbiddenException("Token is invalid")
        }
    }

    override fun delete(token: String, id: Long) {
        if (userService.validate(token)) {
            if (!userService.checkRoleLevel(token, 2, 4)) throw ForbiddenException("You cannot delete articles")
            val articleOptional = newsCrudRepository.findById(id)
            if (articleOptional.isEmpty) {
                throw NotFoundException("Article not found")
            }
            newsCrudRepository.delete(articleOptional.get())
        } else {
            throw ForbiddenException("Token is invalid")
        }
    }
}