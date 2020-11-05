package org.hse.lycsovet.module

import io.jsonwebtoken.*
import lombok.Getter
import org.hse.lycsovet.User
import java.util.*

class JWTToken {

    private val LIFETIME: Long = 86400000L
    private val SECRET: String = "tevoscyl"

    var id: Long
    var login: String
    var issued: Date? = null
    var expiration: Date? = null

    constructor(user: User) {
        id = user.id!!
        login = user.login
    }

    @Throws(exceptionClasses = [ExpiredJwtException::class, InvalidClaimException::class])
    constructor(token: String) {
        val body: Claims = Jwts.parser().setSigningKey(SECRET).parse(token).body as Claims
        login = body.subject
        id = body["id"].toString().toLong()
        issued = body.issuedAt
        expiration = body.expiration
    }

    fun createToken() : String {
        issued = Date()
        expiration = Date(issued!!.time + LIFETIME)

        val claims = Jwts.claims()
        claims.subject = login
        claims["id"] = id
        claims.issuedAt = issued
        claims.expiration = expiration

        return Jwts.builder().addClaims(claims).signWith(SignatureAlgorithm.HS512, SECRET).compact()
    }
}