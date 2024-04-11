package com.itera.plugins

import com.itera.model.Thingy
import com.itera.service.ThingyService
import io.ktor.http.HttpStatusCode
import io.ktor.server.application.Application
import io.ktor.server.application.call
import io.ktor.server.request.receive
import io.ktor.server.response.respond
import io.ktor.server.routing.delete
import io.ktor.server.routing.get
import io.ktor.server.routing.post
import io.ktor.server.routing.route
import io.ktor.server.routing.routing

fun Application.configureThingyRequests(service: ThingyService) {
    routing {
        route("/thingies") {
            get("/") {
                call.respond(service.all())
            }

            get("/{id}") {
                when (val id = call.parameters["id"]?.toInt()) {
                    null -> call.respond(HttpStatusCode.BadRequest)
                    else -> when (val thingy = service.find(id)) {
                        null -> call.respond(HttpStatusCode.NotFound)
                        else -> call.respond(thingy)
                    }
                }
            }

            post("/") {
                val thingy = call.receive<Thingy>()

                when (val newThingy = service.add(thingy)) {
                    null -> call.respond(HttpStatusCode.Conflict)
                    else -> call.respond(newThingy)
                }
            }

            delete("/") {
                when (val id = call.request.queryParameters["id"]?.toInt()) {
                    null -> call.respond(HttpStatusCode.BadRequest)
                    else -> when (val thingy = service.delete(id)) {
                        null -> call.respond(HttpStatusCode.NotFound)
                        else -> call.respond(thingy)
                    }
                }
            }
        }
    }
}