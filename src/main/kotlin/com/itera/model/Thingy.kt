package com.itera.model

import kotlinx.serialization.Serializable

@Serializable
data class Thingy(
    val id: Int,
    val name: String,
)
