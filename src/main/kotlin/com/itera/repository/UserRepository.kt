package com.itera.repository

import com.itera.model.UserClaims

class UserRepository {
    fun hashForUser(username: String) = when (username) {
        // "password" - should be loaded from a database or similar of course
        "valid" -> "\$2b\$12\$Tc8n3Lvjuvv5iF4of0Q.Tu.wAJ9n5EMJWntbtw5NgvaSGAN00H4aK"
        else -> null
    }

    fun claimsForUser(username: String) = when (username) {
        "valid" -> UserClaims(username)
        else -> null
    }
}