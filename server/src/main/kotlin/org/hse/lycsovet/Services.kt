package org.hse.lycsovet

interface AppealService
interface NewsService {
    fun get(id: Long): Article
    fun all(): List<Article>
    fun feed(): List<Article>
    fun publish(articleDTO: ArticleDTO): Long?
    fun publish(id: Long)
    fun edit(articleDTO: ArticleDTO)
    fun delete(id: Long)
}
interface SupportService
interface UserService