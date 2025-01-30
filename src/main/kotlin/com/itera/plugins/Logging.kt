package com.itera.plugins

import io.ktor.server.application.Application
import io.ktor.server.application.install
import io.ktor.server.plugins.callid.CallId
import io.ktor.server.plugins.callid.callIdMdc
import io.ktor.server.plugins.calllogging.CallLogging
import java.util.UUID

fun Application.configureLogging() {
    install(CallLogging) {
        callIdMdc("call-id")
    }

    install(CallId) {
        generate {
            UUID.randomUUID().toString()
        }
    }
}
