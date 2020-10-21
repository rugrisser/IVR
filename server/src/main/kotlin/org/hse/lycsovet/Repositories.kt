package org.hse.lycsovet

import org.springframework.data.repository.CrudRepository
import java.util.*

interface AppealTypeCrudRepository : CrudRepository<AppealType, Long> {
    fun findByName(name: String) : Optional<AppealType>
}
interface AppealStatusCrudRepository : CrudRepository<AppealStatus, Long> {
    fun findByName(name: String) : Optional<AppealStatus>
}
interface UserCrudRepository : CrudRepository<User, Long>
interface NewsCrudRepository : CrudRepository<Article, Long> {
    override fun findAll(): List<Article>
    fun findAllByPublished(published: Boolean) : List<Article>
}
interface AppealCrudRepository : CrudRepository<Appeal, Long>
interface RoleCrudRepository : CrudRepository<Role, Long> {
    fun findByName(name: String) : Optional<Role>
}