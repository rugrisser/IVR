package org.hse.lycsovet

import org.springframework.data.repository.CrudRepository

interface AppealTypeCrudRepository: CrudRepository<AppealType, Long>
interface AppealStatusCrudRepository: CrudRepository<AppealStatus, Long>
interface UserCrudRepository: CrudRepository<User, Long>
interface NewsCrudRepository: CrudRepository<News, Long>
interface AppealCrudRepository: CrudRepository<Appeal, Long>