package com.itera.plugins

import com.auth0.jwt.JWT
import com.auth0.jwt.JWTVerifier
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.auth.authenticate
import io.ktor.server.auth.authentication
import io.ktor.server.auth.jwt.JWTPrincipal
import io.ktor.server.auth.jwt.jwt
import io.ktor.server.auth.principal
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun buildJwtVerifier(config: ApplicationConfig): JWTVerifier =
    JWT
        .require(Algorithm.HMAC256(config.property("jwt.secret").getString()))
        .withAudience(config.property("jwt.audience").getString())
        .withIssuer(config.property("jwt.issuer").getString())
        .build()

fun Application.configureSecurity() {
    val config = environment.config
    val verifier = buildJwtVerifier(config)

    authentication {
        jwt("auth-jwt") {
            realm = config.property("jwt.realm").getString()

            verifier(verifier)

            validate { credential ->
                if (credential.payload.audience.contains(config.property("jwt.audience").getString())) {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }

            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
            }
        }
    }

    routing {
        route("/authorized") {
            authenticate("auth-jwt") {
                get {
                    when (val principal = call.principal<JWTPrincipal>()) {
                        null -> throw UserMissing("Didn't find principal")
                        else -> call.respondText(principal["username"] ?: "No username found")
                    }
                }
            }
        }
    }
}
