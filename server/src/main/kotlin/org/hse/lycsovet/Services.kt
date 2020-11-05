package org.hse.lycsovet

interface AppealService {
    fun get(id: Long): Appeal
    fun all(): List<Appeal>
    fun own(token: String): List<Appeal>
    fun feed(): List<Appeal>
    fun create(token: String, appealDTO: AppealDTO) : Long?
    fun edit(token: String, appealDTO: AppealDTO)
    fun changeStatus(token: String, id: Long, status: String)
}
interface NewsService {
    fun get(id: Long): Article
    fun all(): List<Article>
    fun feed(): List<Article>
    fun publish(token: String, articleDTO: ArticleDTO): Long?
    fun publish(token: String, id: Long)
    fun edit(token: String, articleDTO: ArticleDTO)
    fun delete(token: String, id: Long)
}
interface SupportService {
    fun get(token: String): List<Ticket>
    fun create(token: String, ticketDTO: TicketDTO) : Long?
    fun close(token: String, id: Long, message: String)
}
interface UserService {
    fun login(login: String, password: String) : String
    fun validate(token: String) : Boolean
    fun getRole(token: String) : Role
    fun setRole(token: String, roleName: String, id: Long)
    fun getUser(token: String) : User
    fun getUser(token: String, id: Long) : User
}
interface PollService {
    fun get(id: String): Poll
    fun getListByAppeal(appealID: Long): List<Poll>
    fun answers(token: String, id: String): List<Answer>
    fun create(token: String, poll: PollDTO): String?
    fun publish(token: String, pollID: String)
    fun edit(token: String, poll: PollDTO)
    fun delete(token: String, pollID: String)
    fun answer(token: String, answer: AnswerDTO)
    fun changeAvailability(token: String, pollID: String)
}