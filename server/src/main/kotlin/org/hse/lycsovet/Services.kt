package org.hse.lycsovet

interface AppealService {
    fun get(id: Long): Appeal
    fun all(): List<Appeal>
    fun feed(): List<Appeal>
    fun create()
    fun edit()
    fun changeStatus(status: String)
}
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