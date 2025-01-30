package com.itera.plugins

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.statuspages.StatusPages
import io.ktor.server.response.respond
import io.ktor.server.response.respondText

private val logger = KotlinLogging.logger {}

class FailedLogin(
    message: String,
) : Exception(message)

class ClaimsMissing(
    message: String,
) : Exception(message)

class UserMissing(
    message: String,
) : Exception(message)

fun Application.configureRouting() {
    install(StatusPages) {
        exception<FailedLogin> { call, cause ->
            logger.error(cause) { "Login failed: $cause" }
            call.respond(HttpStatusCode.Unauthorized)
        }

        exception<ClaimsMissing> { call, cause ->
            logger.error(cause) { "Login failed: $cause" }
            call.respond(HttpStatusCode.Forbidden)
        }

        exception<UserMissing> { call, cause ->
            logger.error(cause) { "User not found: $cause" }
            call.respond(HttpStatusCode.Forbidden)
        }

        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause", status = HttpStatusCode.InternalServerError)
        }
    }
}
