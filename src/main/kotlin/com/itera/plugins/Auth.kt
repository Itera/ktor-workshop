package com.itera.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.itera.model.LoginRequest
import com.itera.model.LoginResponse
import com.itera.model.UserClaims
import com.itera.service.UserService
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.server.application.Application
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.post
import io.ktor.server.routing.routing
import java.util.Date

private val logger = KotlinLogging.logger {}
private const val EXPIRY = 1000 * 60 * 20

fun buildToken(
    config: ApplicationConfig,
    claims: UserClaims,
): String =
    JWT
        .create()
        .withAudience(config.property("jwt.audience").getString())
        .withIssuer(config.property("jwt.issuer").getString())
        .withClaim("username", claims.username)
        .withExpiresAt(Date(System.currentTimeMillis() + EXPIRY))
        .sign(Algorithm.HMAC256(config.property("jwt.secret").getString()))

fun Application.configureLoginRouting(service: UserService) {
    val config = environment.config

    routing {
        post("/login") {
            val loginRequest = call.receive<LoginRequest>()

            if (!service.checkPassword(loginRequest.username, loginRequest.password)) {
                logger.debug { "Invalid password for ${loginRequest.username}" }

                throw FailedLogin("Invalid password")
            }

            val claims = service.claims(loginRequest.username) ?: throw ClaimsMissing("Missing claims")

            call.respond(LoginResponse(token = buildToken(config, claims)))
        }
    }
}
