package org.hse.lycsovet

data class ArticleDTO(
        val id: Long?,
        val title: String,
        val description: String,
        val text: String,
        var publish: Boolean?
)