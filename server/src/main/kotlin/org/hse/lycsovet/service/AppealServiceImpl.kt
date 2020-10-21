package org.hse.lycsovet.service

import org.hse.lycsovet.AppealCrudRepository
import org.hse.lycsovet.AppealService
import org.springframework.stereotype.Service

@Service
class AppealServiceImpl (
        private val appealCrudRepository: AppealCrudRepository
) : AppealService {
    
}

