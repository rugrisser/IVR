package org.hse.lycsovet

data class ChangeAppealStatusDTO(
        val id: Long,
        val status: String
)
data class ArticleDTO(
        val id: Long?,
        val title: String,
        val description: String,
        val text: String,
        var publish: Boolean?
)
data class AppealDTO(
        val id: Long?,
        val title: String,
        val text: String,
        val feedback: Int,
        val published: Boolean,
        val type: String
)
data class TicketDTO(
        val text: String
)
data class PollDTO(
        val id: String?,
        val appealID: Long?,
        val available: Boolean,
        val published: Boolean,
        val target: List<String>,
        val content: List<List<Question>>
)
data class AnswerDTO(
        val pollID: String,
        val content: List<List<Any?>>
)
data class SetRoleDTO(
        val name: String
)