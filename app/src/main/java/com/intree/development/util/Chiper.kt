package com.intree.development.util

import java.lang.Exception
import java.util.*
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec
import javax.crypto.Cipher as javaCipher

interface Cipher {
    fun encrypt(plainText: String?): String?
    fun decrypt(cipherText: String?): String?
}

interface CipherFactory {
    fun create(key: String, initVector: String): Cipher
}

/**
 * key: 128 bit
 * initVector: 128 bit
 */
class AESCipher(key: String, initVector: String) : Cipher {
    companion object : CipherFactory {
        override fun create(key: String, initVector: String): Cipher {
            return AESCipher(key, initVector)
        }
    }

    private val encryptCipher: javaCipher
    private val decryptCipher: javaCipher

    init {
        val iv: IvParameterSpec = IvParameterSpec(initVector.toByteArray(charset("UTF-8")))
        val secretKey: SecretKeySpec = SecretKeySpec(key.toByteArray(charset("UTF-8")), "AES")
        encryptCipher = javax.crypto.Cipher.getInstance("AES/GCM/NoPadding")
        encryptCipher.init(javax.crypto.Cipher.ENCRYPT_MODE, secretKey, iv)
        decryptCipher = javax.crypto.Cipher.getInstance("AES_128")
        decryptCipher.init(javax.crypto.Cipher.DECRYPT_MODE, secretKey, iv)
    }

    override fun encrypt(plainText: String?): String? {
        return try {
            String(Base64.getEncoder().encode(encryptCipher.doFinal(plainText?.toByteArray())))
        } catch (e: Exception) {
            null
        }
    }

    override fun decrypt(cipherText: String?): String? {
        return try {
            String(decryptCipher.doFinal(Base64.getDecoder().decode(cipherText)))
        } catch (e: Exception) {
            null
        }
    }
}