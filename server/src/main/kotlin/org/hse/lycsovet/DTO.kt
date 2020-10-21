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