package org.hse.lycsovet.service

import org.hse.lycsovet.Article
import org.hse.lycsovet.ArticleDTO

interface NewsService {
    fun get(id: Long): Article
    fun all(): List<Article>
    fun feed(): List<Article>
    fun publish(articleDTO: ArticleDTO): Long?
    fun publish(id: Long)
    fun edit(articleDTO: ArticleDTO)
    fun delete(id: Long)
}