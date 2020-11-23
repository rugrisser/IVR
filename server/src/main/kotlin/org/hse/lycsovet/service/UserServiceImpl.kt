package org.hse.lycsovet.service

import org.hse.lycsovet.*
import org.hse.lycsovet.http.EljurHTTPResponse
import org.hse.lycsovet.http.EljurHTTPService
import org.hse.lycsovet.http.EljurStudentDetails
import org.hse.lycsovet.http.EljurToken
import org.hse.lycsovet.module.JWTToken
import org.slf4j.Logger
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import retrofit2.Response
import java.io.IOException
import java.util.*

@Service
class UserServiceImpl(
        private val logger: Logger,
        private val userCrudRepository: UserCrudRepository,
        private val roleCrudRepository: RoleCrudRepository,
        @Value("\${eljur.api.vendor}") private val vendor: String,
        @Value("\${eljur.api.devkey}") private val devkey: String,
        @Value("\${eljur.api.format}") private val outFormat: String
) : UserService {

    private val USER_ROLE_NAME = "user"
    private val eljurHTTPService: EljurHTTPService = EljurHTTPService.create()

    override fun login(login: String, password: String): String {
        val request = eljurHTTPService.login(vendor, devkey, outFormat, login, password)
        var response: Response<EljurHTTPResponse<EljurToken>>? = null

        try {
            response = request.execute()
        } catch (ioException: IOException) {
            logger.error("[ELJUR API] GOT ERROR")
            logger.error("[ELJUR API] {}", ioException.message)

            throw InternalServerErrorException("An error occurred while server tried to contact with Eljur API. Try later")
        }

        if (response.code() != 200 && response.code() != 400) {
            logger.error("[ELJUR API] GOT STATUS CODE {}", response.code())
            throw InternalServerErrorException("An error occurred while server tried to contact with Eljur API. Try later")
        }
        if (response.code() == 400) throw ForbiddenException("Incorrect login or password")

        val body = response.body()!!.response
        val token = body.result.token
        val userOptional = userCrudRepository.findByLogin(login)
        val user: User = if (userOptional.isEmpty) {
            updateUserInfo(login, token)
        } else {
            var user = userOptional.get()
            if (!user.actual) user = updateUserInfo(login, token)
            user
        }

        val jwtToken = JWTToken(user)
        return jwtToken.createToken()
    }

    override fun validate(token: String) : Boolean {
        if (!token.startsWith("Bearer ")) return false
        val token = removeTokenPrefix(token)

        return try {
            val jwtToken = JWTToken(token)
            val userOptional: Optional<User> = userCrudRepository.findById(jwtToken.id)

            userOptional.isPresent
        } catch (exception: Exception) {
            logger.error("[JWT] TOKEN IS INVALID")
            logger.error("[JWT] {}", exception.message)
            false
        }
    }

    override fun getRole(token: String): Role {
        if (validate(token)) {
            val token = removeTokenPrefix(token)

            try {
                val jwtToken = JWTToken(token)
                val userOptional: Optional<User> = userCrudRepository.findById(jwtToken.id)

                if (userOptional.isPresent) return userOptional.get().role

                throw ForbiddenException("User not found")
            } catch (exception: Exception) {
                throw ForbiddenException("Token is invalid")
            }
        }

        throw ForbiddenException("Token is invalid")
    }

    override fun setRole(token: String, roleName: String, id: Long) {
        if (validate(token)) {
            val token = removeTokenPrefix(token)

            var jwtToken: JWTToken? = null
            try {
                jwtToken = JWTToken(token)
            } catch (exception: Exception) {
                throw ForbiddenException("Token is invalid")
            }

            val userOptional = userCrudRepository.findById(jwtToken.id)
            if (userOptional.isEmpty) throw ForbiddenException("User not found")
            val user = userOptional.get()

            if (user.role.name != "admin" && user.role.name != "chairman") throw ForbiddenException("Access not granted")
            if (user.id == id) throw BadRequestException("You cannot change your own role")

            val targetUserOptional = userCrudRepository.findById(id)
            if (targetUserOptional.isEmpty) throw NotFoundException("User with given ID not found")
            val targetUser = targetUserOptional.get()

            val roleOptional = roleCrudRepository.findByName(roleName)
            if (roleOptional.isEmpty) throw BadRequestException("Role not found")
            val role = roleOptional.get()
            if (user.role.level < role.level) throw ForbiddenException("You cannot set this role")
            targetUser.role = role
            userCrudRepository.save(user)
        } else {
            throw ForbiddenException("Token is invalid")
        }
    }

    override fun getUser(token: String): User {
        if (validate(token)) {
            val token = removeTokenPrefix(token)

            try {
                val jwtToken = JWTToken(token)
                val userOptional: Optional<User> = userCrudRepository.findById(jwtToken.id)

                if (userOptional.isPresent) return userOptional.get()

                throw ForbiddenException("User not found")
            } catch (exception: Exception) {
                throw ForbiddenException("Token is invalid")
            }
        }

        throw ForbiddenException("Token is invalid")
    }

    override fun getUser(token: String, id: Long): User {
        if (validate(token)) {
            val token = removeTokenPrefix(token)

            try {
                val jwtToken = JWTToken(token)
                val userOptional = userCrudRepository.findById(jwtToken.id)
                if (userOptional.isEmpty) throw ForbiddenException("User not found")
                val user = userOptional.get()

                if (user.role.name != "admin" && user.role.name != "chairman") throw ForbiddenException("Access not granted")
                if (user.id == id) return user

                val targetUserOptional = userCrudRepository.findById(id)
                if (targetUserOptional.isEmpty) throw NotFoundException("User with given ID not found")

                return targetUserOptional.get()
            } catch (exception: Exception) {
                throw ForbiddenException("Token is invalid")
            }
        }

        throw ForbiddenException("Token is invalid")
    }

    override fun getAll(token: String): List<User> {
        if (validate(token)) {
            if (!checkRoleLevel(token, 3, 4)) throw ForbiddenException("You cannot watch all users list")
            return userCrudRepository.findAll()
        }

        throw ForbiddenException("Token is invalid")
    }

    fun checkRoleLevel(token: String, start: Int, end: Int): Boolean {
        if (validate(token)) {
            val token = removeTokenPrefix(token)

            var jwtToken: JWTToken? = null
            try {
                jwtToken = JWTToken(token)
            } catch (exception: Exception) {
                throw ForbiddenException("Token is invalid")
            }

            val userOptional = userCrudRepository.findById(jwtToken.id)
            if (userOptional.isEmpty) return false
            val user = userOptional.get()
            val roleLevel = user.role.level

            return roleLevel in start..end
        }

        return false
    }

    private fun removeTokenPrefix(token: String) : String {
        return token.substring(7)
    }

    private fun updateUserInfo(login: String, token: String) : User {
        val request = eljurHTTPService.getRules(vendor, devkey, outFormat, token)
        var response: Response<EljurHTTPResponse<EljurStudentDetails>>? = null

        try {
            response = request.execute()
        } catch (ioException: IOException) {
            logger.error("[ELJUR API] GOT ERROR")
            logger.error("[ELJUR API] {}", ioException.message)

            throw InternalServerErrorException("An error occurred while server tried to contact with Eljur API. Try later")
        }

        if (response.code() != 200) {
            logger.error("[ELJUR API] GOT STATUS CODE {}", response.code())
            throw InternalServerErrorException("An error occurred while server tried to contact with Eljur API. Try later")
        }

        val body = response.body()!!.response
        val relations = body.result.relations.students
        val keys: List<String> = relations.keys.toList()

        if (keys.isEmpty()) {
            throw InternalServerErrorException("An error occurred while server tried to contact with Eljur API. Try later")
        }

        val relation = relations[keys[0]]
        if (relation!!.rel != "child") throw ForbiddenException("Registration denied")
        val userOptional = userCrudRepository.findByLogin(login)
        val roleOptional = roleCrudRepository.findByName(USER_ROLE_NAME)
        if (roleOptional.isEmpty) {
            throw InternalServerErrorException("User role not found. Try again later")
        }
        val stream: Stream = when(relation.parallel) {
            9 -> Stream.FUTURITET
            else -> {
                when(relation.grade[2]) {
                    'И' -> Stream.MATHINFO
                    'Э' -> Stream.MATHEC
                    'Г' -> Stream.HUM
                    'С' -> Stream.SOCEC
                    'П' -> Stream.PSYSOC
                    'Ю' -> Stream.LAW
                    'В' -> Stream.ORIENTAL
                    'Д' -> Stream.DESIGN
                    'М' -> Stream.MATH
                    'Е' -> Stream.SCIENCE
                    else -> Stream.UNKNOWN
                }
            }
        }
        val user = if (userOptional.isEmpty) {
            val user = User(null, login, roleOptional.get(), relation.parallel, stream, relation.title, true)
            user
        } else {
            userOptional.get()
        }
        user.actual = true
        userCrudRepository.save(user)

        return user
    }

}