package com.headstorm.pongstorm.database

import org.jetbrains.exposed.dao.IntIdTable
import org.jetbrains.exposed.sql.Column
import org.postgresql.util.PGobject

object Players: IntIdTable("players") {
    val rating: Column<Float> = float("rating")
    val volatility: Column<Float> = float("volatility")
    val deviation: Column<Float> = float("deviation")
}

class PGEnum<T: Enum<T>>(enumTypeName: String, enumValue: T?): PGobject() {
    init {
        value = enumValue?.name
        type = enumTypeName
    }
}

object GameOutcomes: IntIdTable("game_outcomes") {
    val playerOneId: Column<Int> = integer("player_one_id")
    val playerTwoId: Column<Int> = integer("player_two_id")
    val winner = customEnumeration("winner", "outcome_type", {value -> }, { PGEnum("outcome_types", it)})
}