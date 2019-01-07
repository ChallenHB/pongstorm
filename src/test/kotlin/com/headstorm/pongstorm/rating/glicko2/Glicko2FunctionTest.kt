package com.headstorm.pongstorm.rating.glicko2

import kotlin.math.exp
import kotlin.test.assertEquals
import org.junit.Test as test
/**
 *
 */
class Glicko2FunctionTest {

    /**
     * The example given in the Glicko2 example paper rounds to four digits, so I am just going to check it's within
     * +- values for each
     * Maybe there's a better way to do this, but for now it's good enough
     */
    fun assertWithinTolerance(expected: Player, actual: Player) {
        assert(actual.rating >= expected.rating - .1 && actual.rating <= expected.rating + .1)
        assert(actual.deviation >= expected.deviation - .1 && actual.deviation <= expected.deviation + .1)
        assert(actual.volatility >= expected.volatility - .005 && actual.volatility <= expected.volatility + .005)
    }

//    "Glicko2 example... Example"
    @test fun Glicko2_main_ex() {
        val player = Player(1, 1500f, 0.06f, 200f)
        val outcome1 = Outcome(
                Player(2, 1400f, 0.06f, 30f),
                OutcomeType.WIN
        )
        val outcome2 = Outcome(
                Player(3, 1550f, 0.06f, 100f),
                OutcomeType.LOSE
        )
        val outcome3 = Outcome(
                Player(4, 1700f, 0.06f, 300f),
                OutcomeType.LOSE
        )

        val playerOut = calculatePlayerScore1(player, listOf(outcome1, outcome2, outcome3))

        assertWithinTolerance(Player(1, 1464.06f, 0.05999f, 151.52f), playerOut)
    }
}