package com.itera.service

import com.itera.model.Thingy
import com.itera.repository.ThingyRepository

class ThingyService(
    private val repository: ThingyRepository,
) {
    fun find(id: Int) = repository.find(id)

    fun add(thingy: Thingy) = repository.add(thingy)

    fun all() = repository.all()

    fun delete(id: Int) = repository.delete(id)
}
