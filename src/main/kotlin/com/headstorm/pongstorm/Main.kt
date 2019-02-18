package com.headstorm.pongstorm

import com.headstorm.pongstorm.modules.ratingModule
import io.ktor.application.Application
import io.ktor.server.engine.embeddedServer
import io.ktor.server.netty.Netty

fun main(args: Array<String>) {
    val server = embeddedServer(Netty, port = 8080, module = Application::ratingModule)
    server.start(wait = true)
}
