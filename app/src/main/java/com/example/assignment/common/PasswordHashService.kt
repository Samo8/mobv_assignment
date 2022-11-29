package com.example.assignment.common

import java.math.BigInteger
import java.security.MessageDigest

class PasswordHashService {
    fun getSHA512(input:String):String{
        val md: MessageDigest = MessageDigest.getInstance("SHA-512")
        val messageDigest = md.digest(input.toByteArray())

        val no = BigInteger(1, messageDigest)
        var hashtext: String = no.toString(16)

        while (hashtext.length < 128) {
            hashtext = "0$hashtext"
        }
        return hashtext
    }
}