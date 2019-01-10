package com.headstorm.pongstorm.modules

import io.ktor.application.Application
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.routing.get
import io.ktor.routing.post
import io.ktor.routing.routing
/**
 *
 */

fun Application.ratingModule() {
    install(ContentNegotiation) {
        gson{}
    }

    routing {
        get("/player") {

        }
        post("/routing") {

        }
    }

}
