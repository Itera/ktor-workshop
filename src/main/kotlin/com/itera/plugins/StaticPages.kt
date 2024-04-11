package com.itera.plugins

import io.ktor.server.application.Application
import io.ktor.server.http.content.staticFiles
import io.ktor.server.http.content.staticResources
import io.ktor.server.routing.routing
import java.io.File

fun Application.configureStaticPages() {
    routing {
        staticFiles("/static", File("content"))
        staticResources("/staticresource", "staticcontent")
    }
}