package com.itera.plugins

import com.itera.model.CatFact
import io.github.oshai.kotlinlogging.KotlinLogging
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.server.application.Application
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.response.respondText
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing
import kotlinx.serialization.Serializable

private val logger = KotlinLogging.logger {}

@Serializable
data class SimpleRequestBody(
    val name: String,
    val message: String,
)

fun Application.configureSimpleRequests() {
    routing {
        get("/get_1") {
            call.respondText("Get 1")
        }

        get("/pathParam/{paramName}") {
            val param = call.parameters["paramName"] ?: "No paramName supplied"

            logger.debug { "Path param: $param" }

            call.respondText(param)
        }

        get("/queryParam") {
            val param = call.request.queryParameters["paramName"] ?: "No paramName supplied"

            logger.debug { "Query param: $param" }

            call.respondText(param)
        }

        route("/simpleBody") {
            get {
                call.respond(SimpleRequestBody("Hi", "There"))
            }
            post {
                val body = call.receive<SimpleRequestBody>()
                call.respond(body)
            }
        }

        get("catFact") {
            call.respond(catClient.get("https://catfact.ninja/fact").body<CatFact>())
        }
    }
}
