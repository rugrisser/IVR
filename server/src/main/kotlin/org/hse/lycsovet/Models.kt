package org.hse.lycsovet

import com.sun.istack.NotNull
import javax.persistence.*

@Entity
@Table(name = "appeal_types")
data class AppealType(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long,

        val name: String
)

@Entity
@Table(name = "appeal_statuses")
data class AppealStatus(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long,

        val name: String,
        val milestone: Long
)

@Entity
@Table(name = "users")
data class User(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long,

        @Column(unique = true)
        val email: String
)

@Entity
@Table(name = "news")
data class News(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long,

        val title: String,
        @Lob
        val description: String,

        @Lob
        val text: String
)

@Entity
@Table(name = "appeals")
data class Appeal(
        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        val id: Long,

        val title: String,
        @Lob
        val text: String,
        val feedback: Int,
        @ManyToOne
        val type: AppealType,
        @ManyToOne
        val status: AppealStatus
)