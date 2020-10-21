package org.hse.lycsovet.service

import org.hse.lycsovet.*
import org.springframework.stereotype.Service
import java.util.*

@Service
class NewsServiceImpl(
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

    override fun publish(articleDTO: ArticleDTO): Long? {
        if (articleDTO.publish == null) {
            articleDTO.publish = false
        }
        val article = Article(null, articleDTO.title, articleDTO.description, articleDTO.text, articleDTO.publish!!)
        newsCrudRepository.save(article)
        return article.id
    }

    override fun publish(id: Long) {
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
    }

    override fun edit(articleDTO: ArticleDTO) {
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
    }

    override fun delete(id: Long) {
        val articleOptional = newsCrudRepository.findById(id)
        if (articleOptional.isEmpty) {
            throw NotFoundException("Article not found")
        }
        newsCrudRepository.delete(articleOptional.get())
    }
}