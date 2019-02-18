package com.headstorm.pongstorm.repositories

import com.headstorm.pongstorm.rating.glicko2.Player

class PlayerRepository {
    fun getPlayer(id: Int): Player {
        if (id == 1)
            return Player(1, 1500f, 0.06f, 200f)
        return Player(2, 1400f, 0.06f, 200f)
    }
}
