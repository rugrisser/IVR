package org.hse.lycsovet.service

import org.hse.lycsovet.Appeal
import org.hse.lycsovet.AppealCrudRepository
import org.hse.lycsovet.AppealService
import org.springframework.stereotype.Service

@Service
class AppealServiceImpl (
        private val appealCrudRepository: AppealCrudRepository
) : AppealService {
    override fun get(id: Long): Appeal {
        TODO("Not yet implemented")
    }

    override fun all(): List<Appeal> {
        TODO("Not yet implemented")
    }

    override fun feed(): List<Appeal> {
        TODO("Not yet implemented")
    }

    override fun create() {
        TODO("Not yet implemented")
    }

    override fun edit() {
        TODO("Not yet implemented")
    }

    override fun changeStatus(status: String) {
        TODO("Not yet implemented")
    }
}

