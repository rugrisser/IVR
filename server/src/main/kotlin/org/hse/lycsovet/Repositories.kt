package org.hse.lycsovet

import org.springframework.data.repository.CrudRepository

interface AppealTypeCrudRepository: CrudRepository<AppealType, Long>
interface AppealStatusCrudRepository: CrudRepository<AppealStatus, Long>
interface UserCrudRepository: CrudRepository<User, Long>
interface NewsCrudRepository: CrudRepository<Article, Long> {
    override fun findAll(): List<Article>
    fun findAllByPublished(published: Boolean): List<Article>
}
interface AppealCrudRepository: CrudRepository<Appeal, Long>