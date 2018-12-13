package com.headstorm.pongstorm.rating.glicko2

import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.pow

val epsilon = 0.000001

// This function will take glicko2 scores, not glicko scores
fun calculatePlayerScore(player: Player, results: List<Result>): Player {


    val v = results.map{ vSumFunction(player, it) }.reduce{ acc, it -> acc + it }.pow(-1)
    val delta = results.map{ deltaSumFunction(player, it) }.reduce{ acc, it -> acc + it }.times(v)
    return Player(123, 1f, 1f, 1f)
}

// Function to be called in summation
fun vSumFunction(player: Player, result: Result): Float {
    val playerRating = player.rating
    val oppRating = result.opponent.rating
    val oppDeviation = result.opponent.deviation

    val gValue = gFunction(oppDeviation)
    val eValue = eFunction(playerRating, oppRating, oppDeviation)

    return gValue.pow(2) * eValue * (1 - eValue)
}

fun deltaSumFunction(player: Player, result: Result): Float {
    val playerRating = player.rating
    val oppRating = result.opponent.rating
    val oppDeviation = result.opponent.rating

    val gValue = gFunction(oppDeviation)
    val eValue = eFunction(playerRating, oppRating, oppDeviation)

    return gValue * (result.type.score - eValue)
}

fun volatilityIterativeFn(deviation: Float, v: Float, delta: Float, tao: Float): (Float, Float) -> Float = { x, a ->
    val num1 = exp(x) * (delta.pow(2) - deviation.pow(2) - v - exp(x))
    val den1 = 2 * (deviation.pow(2) + v + exp(x)).pow(2)

    val num2 = x - a
    val den2 = tao.pow(2)

     num1 / den1 - num2 / den2
}

fun eFunction(rating: Float, oppRating: Float, oppDeviation: Float): Float {

    return (1 + exp(-1 * gFunction(oppDeviation) * (rating - oppRating))).pow(-1)
}

// Is it faster to 1/val or val.pow(-1)? (benchmarks)
fun gFunction(deviation: Float): Float {

    return (1 + 3 * deviation.pow(2) / PI).toFloat().pow(-1)
}

