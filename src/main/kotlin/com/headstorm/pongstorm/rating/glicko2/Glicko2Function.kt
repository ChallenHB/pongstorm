package com.headstorm.pongstorm.rating.glicko2

import kotlin.math.*

const val epsilon = 0.000001

fun calculatePlayerScore1(player: Player, results: List<Outcome>): Player {
    val outPlayer = calculatePlayerScore(toGlicko2(player), results.map{ it.copy(opponent = toGlicko2(it.opponent)) })

    return toGlicko1(outPlayer)
}

// This function will take glicko2 scores, not glicko scores
fun calculatePlayerScore(player: Player, results: List<Outcome>): Player {
    // Is this needed? Determine if this is just naturally handled.
    if (results.isEmpty()) {
        val deviation = sqrt(player.deviation.pow(2) + player.volatility.pow(2))
        return Player(player.id, player.rating, player.volatility, deviation)
    }

    val v = results.map{ vSumFunction(player, it) }.reduce{ acc, it -> acc + it }.pow(-1)
    val delta = results.map{ deltaSumFunction(player, it) }.reduce{ acc, it -> acc + it }.times(v)
    val volatility = calculateVolatility(player.volatility, player.deviation, v, delta, 0.06f)
    val deviationStar = sqrt(player.deviation.pow(2) + volatility.pow(2))
    val deviation = sqrt(deviationStar.pow(-2) + v.pow(-1)).pow(-1)
    val rating = player.rating + deviation.pow(2) * results.map{ deltaSumFunction(player, it) }.reduce{ acc, it -> acc + it }
    return Player(player.id, rating, volatility, deviation)
}

fun toGlicko2(player: Player): Player {
    return player.copy(
            rating = player.rating.minus(1500).div(173.7178).toFloat(),
            deviation = player.deviation.div(173.7178).toFloat()

    )
}

fun toGlicko1(player: Player): Player {
    return player.copy(
            rating = player.rating.times(173.7178).plus(1500).toFloat(),
            deviation = player.deviation.times(173.7178).toFloat()
    )
}

fun vSumFunction(player: Player, result: Outcome): Float {
    val playerRating = player.rating
    val oppRating = result.opponent.rating
    val oppDeviation = result.opponent.deviation

    val gValue = gFunction(oppDeviation)
    val eValue = eFunction(playerRating, oppRating, oppDeviation)

    return gValue.pow(2) * eValue * (1 - eValue)
}

fun deltaSumFunction(player: Player, result: Outcome): Float {
    val playerRating = player.rating
    val oppRating = result.opponent.rating
    val oppDeviation = result.opponent.deviation

    val gValue = gFunction(oppDeviation)
    val eValue = eFunction(playerRating, oppRating, oppDeviation)

    return gValue * (result.type.score - eValue)
}

fun calculateVolatility(volatility: Float, deviation: Float, v: Float, delta: Float, tao: Float): Float {
    val a = ln(volatility.pow(2))
    val fx = createIterativeDeviationFn(deviation, v, delta, tao, a)
    val d2 = delta.pow(2)
    val b =
        if (d2 > deviation.pow(2) + v)
            ln(d2 - deviation.pow(2) - v)
        else {
                var k = 1
                while (fx(a - k * tao) < 0) {
                   k++
                }
                a - k * tao
            }
    var A = a
    var B = b
    var fA = fx(A)
    var fB = fx(B)
    while (abs(B - A) > epsilon) {
        val C = A + (A - B) * fA / (fB - fA)
        val fC = fx(C)
        when {
            fC * fB < 0 -> {
                A = B
                fA = fx(A)
            }
            else -> fA /= 2
        }
        B = C
        fB = fC
    }

    return exp(A / 2)
}

fun createIterativeDeviationFn(deviation: Float, v: Float, delta: Float, tao: Float, a: Float): (Float) -> Float =
{ x ->
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
    return sqrt(1 + 3 * deviation.pow(2) / PI.pow(2)).pow(-1).toFloat()
}
