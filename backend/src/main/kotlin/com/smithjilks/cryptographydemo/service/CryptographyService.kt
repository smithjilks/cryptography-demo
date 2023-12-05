package com.smithjilks.cryptographydemo.service

import com.smithjilks.cryptographydemo.entity.KeyPair
import mu.KLogging
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.nio.charset.StandardCharsets
import java.security.*
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.RSAKeyGenParameterSpec
import java.security.spec.X509EncodedKeySpec
import java.util.*
import javax.crypto.Cipher


@Service
class CryptographyService {
    companion object : KLogging()

    @Value("\${server_secret_key}")
    private lateinit var SECRET_KEY: String

    private val cipher: Cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding")


    fun decryptUsingRsa(encryptedString: String, privateKey: String): String? {
        //Returns string
        val encryptedStringBytes = Base64.getDecoder().decode(encryptedString.toByteArray(StandardCharsets.UTF_8))
        val key: PrivateKey? = generatePrivateKeyFromEncodedString(privateKey)

        return key?.let {
            cipher.init(Cipher.DECRYPT_MODE, it)
            String(cipher.doFinal(encryptedStringBytes))
        }
    }


    fun encryptUsingRsa(unEncryptedString: String, publicKey: String): String? {
        //Returns base64 encoded string
        val key: PublicKey? = generatePublicKeyFromEncodedString(publicKey)
        return key?.let {
            cipher.init(Cipher.ENCRYPT_MODE, it)
            Base64.getEncoder().encodeToString(
                cipher.doFinal(
                    unEncryptedString.toByteArray(StandardCharsets.UTF_8)
                )
            )
        }
    }

    fun generateRSAKeyPair(): KeyPair? {
        return try {
            val keyPairs = KeyPairGenerator.getInstance("RSA").apply {
                initialize(RSAKeyGenParameterSpec(2048, RSAKeyGenParameterSpec.F4), SecureRandom())
            }.genKeyPair()

            val encodedServerPublicKey = Base64.getEncoder().encodeToString(keyPairs.public.encoded)
            val encodedServerPrivateKey =
                Base64.getEncoder().encodeToString(keyPairs.private.encoded)

            val keyPair = KeyPair(
                publicKey = encodedServerPublicKey,
                privateKey = encodedServerPrivateKey
            )

            keyPair
        } catch (e: Exception) {
            logger.error("RSA key gen exception: ${e.message}")
            null
        }

    }

    fun generatePrivateKeyFromEncodedString(encodedPrivateKeyString: String): PrivateKey? {
        return try {
            val bytes = Base64.getDecoder().decode(encodedPrivateKeyString)
            KeyFactory.getInstance("RSA").generatePrivate(PKCS8EncodedKeySpec(bytes))

        } catch (e: Exception) {
            logger.error("Extracting Private Key From Encoded String Exception: ${e.message}")
            null
        }

    }

    fun generatePublicKeyFromEncodedString(encodedPublicKeyString: String): PublicKey? {
        return try {
            val bytes = Base64.getDecoder().decode(encodedPublicKeyString)
            KeyFactory.getInstance("RSA").generatePublic(X509EncodedKeySpec(bytes))
        } catch (e: Exception) {
            logger.error("Extracting Public Key From Encoded String Exception: ${e.message}")
            null
        }
    }

    fun generateServerSigningKey(publicKey: String) = encryptUsingRsa(SECRET_KEY, publicKey)



}