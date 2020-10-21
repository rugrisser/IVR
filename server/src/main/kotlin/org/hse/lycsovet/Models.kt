package org.hse.lycsovet

import org.springframework.data.annotation.CreatedDate
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "appeal_types")
data class AppealType(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long?,

        val name: String
)

@Entity
@Table(name = "appeal_statuses")
data class AppealStatus(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long?,

        val name: String,
        val milestone: Long
)

@Entity
@Table(name = "users")
data class User(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long?,

        @Column(unique = true)
        val email: String,
        val created: Date = Date()
)

@Entity
@Table(name = "articles")
data class Article(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long?,
        var title: String,
        @Column(columnDefinition = "text")
        var description: String,

        @Column(columnDefinition = "text")
        var text: String,
        var published: Boolean,
        val created: Date = Date(),
        var updated: Date = Date()
)

@Entity
@Table(name = "appeals")
data class Appeal(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long?,

        val title: String,
        @Column(columnDefinition = "text")
        val text: String,
        val feedback: Int,
        @ManyToOne
        val type: AppealType,
        @ManyToOne
        val status: AppealStatus,
        val created: Date = Date(),
        val updated: Date = Date()
)