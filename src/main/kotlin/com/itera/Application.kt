package com.itera

import com.itera.plugins.configureLogging
import com.itera.plugins.configureLoginRouting
import com.itera.plugins.configureRouting
import com.itera.plugins.configureSecurity
import com.itera.plugins.configureSerialization
import com.itera.plugins.configureSimpleRequests
import com.itera.plugins.configureStaticPages
import com.itera.plugins.configureThingyRequests
import com.itera.repository.ThingyRepository
import com.itera.repository.UserRepository
import com.itera.service.ThingyService
import com.itera.service.UserService
import io.ktor.server.application.Application
import io.ktor.server.cio.EngineMain

fun main(args: Array<String>): Unit = EngineMain.main(args)

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
