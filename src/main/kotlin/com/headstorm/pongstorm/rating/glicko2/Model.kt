package com.headstorm.pongstorm.rating.glicko2

/**
 *
 */

data class Player(val id: Int, val rating: Float, val volatility: Float, val deviation: Float)

data class Result(val player: Player, val opponent: Player, val type: ResultType)

enum class ResultType(val score: Float){
    WIN(1.0f),
    LOSE(0f),
    DRAW(.5f),
}
