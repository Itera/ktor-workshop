package com.itera.plugins

import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

private val logger = KotlinLogging.logger {}

class FailedLogin(message: String) : Exception(message)

fun Application.configureRouting() {
    install(StatusPages) {
        exception<FailedLogin> { call, cause ->
            logger.error(cause) { "Login failed due to $cause" }
            call.respond(HttpStatusCode.Unauthorized)
        }

        exception<Throwable> { call, cause ->
            call.respondText(text = "500: $cause" , status = HttpStatusCode.InternalServerError)
        }
    }
}
