package com.headstorm.pongstorm.rating.glicko2

import kotlin.math.PI
import kotlin.math.exp
import kotlin.math.pow

// This function will take glicko2 scores, not glicko scores
// For this reason, we will not do the conversion
fun calculatePlayerScore(player: Player, results: List<Result>): Player {



    return Player(123, 1f, 1f, 1f)
}

// Computes the sum
fun computeVSigma(player: Player, results: List<Result>): Float {

    // Using inline function looks less bad than function reference
    val oneOverVSum = results.map{ vSummedFunction(player, it) }.reduce{ acc, it -> acc + it}
    return oneOverVSum.pow(-1)
}

// Function to be called in summation
fun vSummedFunction(player: Player, result: Result): Float {
    val playerRating = player.rating
    val oppRating = result.opponent.rating
    val oppDeviation = result.opponent.deviation

    val gValue = gFunction(oppDeviation)
    val eValue = eFunction(playerRating, oppRating, oppDeviation, result.type.score)

    return gValue.pow(2) * eValue * (1 - eValue)
}

fun eFunction(rating: Float, oppRating: Float, oppDeviation: Float, score: Float): Float {

    return (1 + exp(-1 * gFunction(oppDeviation) * (rating - oppRating))).pow(-1)
}

// Is it faster to 1/val or val.pow(-1)? (benchmarks)
fun gFunction(deviation: Float): Float {

    return (1 + 3 * deviation.pow(2) / PI).toFloat().pow(-1)
}
