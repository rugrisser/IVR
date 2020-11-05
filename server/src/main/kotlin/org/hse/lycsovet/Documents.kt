package org.hse.lycsovet

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document
import java.util.*

enum class QuestionType(val type: Int) {
    TEXT(0),
    RADIO(1),
    CHECKBOXES(2),
    SCALE(3)
}

data class Question(
        val name: String,
        val type: QuestionType,
        val compulsory: Boolean,
        val mixed: Map<String, Any?>
)

@Document(collection = "polls")
data class Poll(
        @Id
        val id: String? = ObjectId().toString(),
        val appealID: Long?,
        var available: Boolean,
        var published: Boolean,
        var target: List<String>,
        var content: List<List<Question>>,
        val created: Date = Date(),
        var updated: Date = Date()
) {
    constructor(dto: PollDTO) : this(ObjectId().toString(), dto.appealID, dto.available, dto.published, dto.target, dto.content)
    fun update(dto: PollDTO) {
        this.target = dto.target
        this.content = dto.content
        this.updated = Date()
    }
}

@Document(collection = "answers")
data class Answer(
        @Id
        val id: String? = ObjectId().toString(),
        val userID: Long,
        val pollID: String,
        var content: List<List<Any?>>,
        val created: Date = Date()
) {
    constructor(dto: AnswerDTO, userID: Long) : this(ObjectId().toString(), userID, dto.pollID, dto.content)
}