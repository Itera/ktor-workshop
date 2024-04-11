package com.itera.repository

import com.itera.model.Thingy
import io.github.oshai.kotlinlogging.KotlinLogging

private val logger = KotlinLogging.logger {}

class ThingyRepository {
    private val thingies = mutableListOf(
        Thingy(id = 1, name = "Thingy 1"),
        Thingy(id = 2, name = "Thingy 2"),
        Thingy(id = 3, name = "Thingy 3"),
        Thingy(id = 4, name = "Thingy 4"),
    )

    fun find(id: Int) = thingies.find { t -> t.id == id }

    fun add(thingy: Thingy) = when (find(thingy.id)) {
        null -> {
            thingies.add(thingy)
            thingy
        }

        else -> {
            logger.info { "Thingy ${thingy.id} already exists" }
            null
        }
    }

    fun all() = thingies.toList()

    fun delete(id: Int) = find(id)?.let { thingy ->
        thingies.remove(thingy)
        thingy
    }
}