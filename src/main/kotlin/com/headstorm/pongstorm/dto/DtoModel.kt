package com.headstorm.pongstorm.dto

// winner is null if drawn
data class OutcomeDto(val playerOneId: Int, val playerTwoId: Int, val winner: Int?)
