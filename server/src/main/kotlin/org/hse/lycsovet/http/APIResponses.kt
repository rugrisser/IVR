package org.hse.lycsovet.http

import com.fasterxml.jackson.annotation.JsonAlias
import com.google.gson.annotations.SerializedName

data class EljurHTTPResponse<T> (
        val response: EljurResponse<T>
)

data class EljurResponse<T> (
        val state: Int,
        val error: String,
        val result: T
)

data class EljurRelation(
        val title: String,
        @SerializedName(value = "class")
        val grade: String,
        val parallel: Int,
        val rel: String
)

data class EljurRelations(
        val students: Map<String, EljurRelation>
)

data class EljurStudentDetails (
        val relations: EljurRelations
)

data class EljurToken (
        val token: String
)