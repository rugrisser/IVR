package org.hse.lycsovet

interface AppealService {
    fun get(id: Long): Appeal
    fun all(): List<Appeal>
    fun own(): List<Appeal>
    fun feed(): List<Appeal>
    fun create(appealDTO: AppealDTO) : Long?
    fun edit(appealDTO: AppealDTO)
    fun changeStatus(id: Long, status: String)
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
interface SupportService {
    fun get(): List<Ticket>
    fun create(ticketDTO: TicketDTO) : Long?
    fun close(id: Long, message: String)
}
interface UserService