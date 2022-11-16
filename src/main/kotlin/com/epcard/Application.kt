package com.epcard

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.epcard.plugins.*
import io.ktor.server.application.*

fun main() {
    embeddedServer(Netty, port = 8080, host = "127.0.0.1") {
        configureSecurity()
        configureMonitoring()
        configureSerialization()
        configureRouting()
    }.start(wait = true)
}
