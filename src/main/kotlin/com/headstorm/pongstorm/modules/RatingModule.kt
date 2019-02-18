package com.headstorm.pongstorm.modules

import com.headstorm.pongstorm.dto.OutcomeDto
import com.headstorm.pongstorm.rating.glicko2.GameOutcome
import com.headstorm.pongstorm.rating.glicko2.OutcomeType
import com.headstorm.pongstorm.rating.glicko2.calculatePlayerScoreGlicko
import com.headstorm.pongstorm.repositories.PlayerRepository
import io.ktor.application.Application
import io.ktor.application.call
import io.ktor.application.install
import io.ktor.features.ContentNegotiation
import io.ktor.gson.gson
import io.ktor.request.receive
import io.ktor.routing.post
import io.ktor.routing.routing
import java.text.DateFormat

/**
 *
 */

val playerRepo = PlayerRepository()

fun Application.ratingModule() {
    install(ContentNegotiation) {
        gson{
            setDateFormat(DateFormat.LONG)
            setPrettyPrinting()
        }
    }

    routing {
        post("/result") {
            val match = call.receive<OutcomeDto>()
            val winner = match.winner
            val playerOne = playerRepo.getPlayer(match.playerOneId)
            val playerTwo = playerRepo.getPlayer(match.playerTwoId)
            val playerOneOutcome: OutcomeType
            val playerTwoOutcome: OutcomeType
            when (winner) {
                playerOne.id -> {
                    playerOneOutcome = OutcomeType.WIN
                    playerTwoOutcome = OutcomeType.LOSE
                }
                playerTwo.id -> {
                    playerOneOutcome = OutcomeType.LOSE
                    playerTwoOutcome = OutcomeType.WIN
                }
                else -> {
                    playerOneOutcome = OutcomeType.DRAW
                    playerTwoOutcome = OutcomeType.DRAW
                }
            }
            val playerOneResults = listOf(
                    GameOutcome(playerTwo, playerOneOutcome)
            )
            val playerTwoResults = listOf(
                    GameOutcome(playerOne, playerTwoOutcome)
            )
            // Maybe use coroutines here
            val newPlayerOne = calculatePlayerScoreGlicko(playerTwo, playerOneResults)

        }

    }

}
