package com.itera

import com.itera.plugins.*
import com.itera.repository.ThingyRepository
import com.itera.repository.UserRepository
import com.itera.service.ThingyService
import com.itera.service.UserService
import io.ktor.server.application.*
import io.ktor.server.cio.*
import io.ktor.server.config.ApplicationConfig
import io.ktor.server.engine.*

fun main() {
    val env = applicationEngineEnvironment {
        connector {
            host = "0.0.0.0"
            port = 8080
        }
        config = ApplicationConfig("application.conf")
        module {
            module()
        }
    }

    embeddedServer(CIO, env).start(wait = true)

}

fun Application.module() {
    configureSerialization()
    configureRouting()
    configureStaticPages()
    configureSimpleRequests()
    configureLogging()

    val thingyRepository = ThingyRepository()
    val thingyService = ThingyService(thingyRepository)
    configureThingyRequests(thingyService)

    val userRepository = UserRepository()
    val userService = UserService(userRepository)
    configureLoginRouting(userService)

    configureSecurity()
}
