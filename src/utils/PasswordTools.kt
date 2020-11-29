package dev.remylavergne.spotfinder.utils

import de.mkammerer.argon2.Argon2Factory
import java.util.*


object PasswordTools {

    fun generatePassword(): String {
        val alphabet = ('0'..'9').joinToString("") +
                ('a'..'z').joinToString("") +
                ('A'..'Z').joinToString("") +
                "!@#$%&"
        val a = alphabet.length

        val rand = Random()

        return generateSequence { rand.nextInt(a - 1) }
            .take(10)
            .map { alphabet[it] }
            .joinToString("")
    }

    fun generateHash(password: String): String? {
        val argon2 = Argon2Factory.create()
        val p: CharArray
        val hash: String
        try {
            p = password.toCharArray()
            hash = argon2.hash(10, 65536, 1, p)
            argon2.wipeArray(p)
        } catch (e: Exception) {
            println(e)
            throw Exception("Generating hash error")
        }

        return hash
    }

    fun isPasswordMatching(passwordHash: String, passwordClear: String): Boolean {
        val argon2 = Argon2Factory.create()
        var p: CharArray = charArrayOf()
        try {
            p = passwordClear.toCharArray()
            return argon2.verify(passwordHash, p)
        } catch (e: Exception) {
            println(e)
            throw Exception("Generating hash error")
        } finally {
            argon2.wipeArray(p)
        }
    }


}