package org.hse.lycsovet

import org.slf4j.Logger
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component

@Component
class DataLoader(
        private val logger: Logger,
        private val roleCrudRepository: RoleCrudRepository,
        private val userCrudRepository: UserCrudRepository,
        private val appealTypeCrudRepository: AppealTypeCrudRepository,
        private val appealStatusCrudRepository: AppealStatusCrudRepository
) : ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        logger.info("[DATA LOADER] Init")
        createRoles()
//        createTestUsers()
        createAppealTypes()
        createAppealStatuses()
    }

    private fun createRoles() {
        logger.info("[DATA LOADER] Role creating start")

        val roles: Array<Role> = arrayOf(
            Role(null, "user", 1),
            Role(null, "deputy", 2),
            Role(null, "chairman", 3),
            Role(null, "admin", 4)
        )

        roles.forEach { role: Role ->
            val roleOptional = roleCrudRepository.findByName(role.name)
            if (roleOptional.isEmpty) {
                roleCrudRepository.save(role)
            }
        }

        logger.info("[DATA LOADER] Role creating end")
    }

    private fun createTestUsers() {
        logger.info("[DATA LOADER] Test user creating start")

        val userRoleOptional = roleCrudRepository.findByName("user")
        val adminRoleOptional = roleCrudRepository.findByName("admin")
        val chairmanRoleOptional = roleCrudRepository.findByName("chairman")

        if (userRoleOptional.isEmpty) {
            throw Exception("Role not found")
        }
        if (adminRoleOptional.isEmpty) {
            throw Exception("Role not found")
        }
        if (chairmanRoleOptional.isEmpty) {
            throw Exception("Role not found")
        }

        val userRole = userRoleOptional.get()
        val adminRole = adminRoleOptional.get()
        val chairmanRole = chairmanRoleOptional.get()

        /*val users: Array<User> = arrayOf(
                User(null, "student@edu.hse.ru", userRole),
                User(null, "admin@edu.hse.ru", adminRole),
                User(null, "chairman@edu.hse.ru", chairmanRole)
        )
        users.forEach { user: User ->
            val userOptional = userCrudRepository.findByLogin(user.login)
            if (userOptional.isEmpty) {
                userCrudRepository.save(user)
            }
        }*/

        logger.info("[DATA LOADER] Test user creating end")
    }

    private fun createAppealTypes() {
        logger.info("[DATA LOADER] Appeal types creating start")

        val types: Array<AppealType> = arrayOf(
                AppealType(null, "proposal"),
                AppealType(null, "complaint")
        )
        types.forEach { type: AppealType ->
            val appealTypeOptional = appealTypeCrudRepository.findByName(type.name)
            if (appealTypeOptional.isEmpty) {
                appealTypeCrudRepository.save(type)
            }
        }

        logger.info("[DATA LOADER] Appeal types creating end")
    }

    private fun createAppealStatuses() {
        logger.info("[DATA LOADER] Appeal statuses creating start")

        val statuses: Array<AppealStatus> = arrayOf(
                AppealStatus(null, "moderation", 1),
                AppealStatus(null, "consideration", 2),
                AppealStatus(null, "reviewed", 3),
                AppealStatus(null, "rejected", 3)
        )
        statuses.forEach { status: AppealStatus ->
            val appealStatusOptional = appealStatusCrudRepository.findByName(status.name)
            if (appealStatusOptional.isEmpty) {
                appealStatusCrudRepository.save(status)
            }
        }

        logger.info("[DATA LOADER] Appeal statuses creating end")
    }
}