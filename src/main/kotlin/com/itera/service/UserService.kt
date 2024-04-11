package com.itera.service

import com.itera.repository.UserRepository
import com.password4j.Password

class UserService(private val repository: UserRepository) {
    fun checkPassword(username: String, password: String) = repository.hashForUser(username)?.let { dbPassword ->
        Password.check(password, dbPassword).withBcrypt()
    } ?: false

    fun claims(username: String) = repository.claimsForUser(username)
}