package com.smithjilks.cryptographydemo.repository.crypto

import android.os.Build
import android.util.Base64
import androidx.annotation.RequiresApi
import com.haroldadmin.cnradapter.NetworkResponse
import com.smithjilks.cryptographydemo.data.DataStoreManager
import com.smithjilks.cryptographydemo.network.AppApiService
import com.smithjilks.cryptographydemo.network.Payload
import com.smithjilks.cryptographydemo.network.response.DefaultErrorResponse
import com.smithjilks.cryptographydemo.network.response.KeyExchangeResponse
import timber.log.Timber
import java.nio.charset.StandardCharsets
import java.security.KeyFactory
import java.security.KeyPairGenerator
import java.security.PrivateKey
import java.security.PublicKey
import java.security.SecureRandom
import java.security.spec.PKCS8EncodedKeySpec
import java.security.spec.RSAKeyGenParameterSpec
import java.security.spec.X509EncodedKeySpec
import javax.crypto.Cipher
import javax.inject.Inject

class CryptoRepository @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val apiService: AppApiService
) {

    private val cipher: Cipher = Cipher.getInstance("RSA/ECB/OAEPWithSHA-1AndMGF1Padding")


    fun decryptUsingRsa(encryptedString: String, privateKey: String): String? {
        //Returns string
        val encryptedStringBytes = Base64.decode(encryptedString.toByteArray(StandardCharsets.UTF_8), Base64.NO_WRAP)
        val key: PrivateKey? = generatePrivateKeyFromEncodedString(privateKey)

        return try {
            key?.let {
                cipher.init(Cipher.DECRYPT_MODE, it)
                val byteArray = cipher.doFinal(encryptedStringBytes)
                String(byteArray, Charsets.UTF_8)
            }
        } catch (e: Exception) {
            Timber.e(e.message)
            null
        }
    }

    fun encryptUsingRsa(unEncryptedString: String, publicKey: String): String? {
        //Returns base64 encoded string
        val key: PublicKey? = generatePublicKeyFromEncodedString(publicKey)
        return key?.let {
            cipher.init(Cipher.ENCRYPT_MODE, it)
            Base64.encodeToString(
                cipher.doFinal(unEncryptedString.toByteArray(StandardCharsets.UTF_8)),
                Base64.NO_WRAP
            )
        }
    }

    suspend fun generateRSAKeyPairAndSave() {
        try {
            val keyPairs = KeyPairGenerator.getInstance("RSA").apply {
                initialize(RSAKeyGenParameterSpec(2048, RSAKeyGenParameterSpec.F4), SecureRandom())
            }.genKeyPair()

            val encodedUiPublicKey = Base64.encodeToString(keyPairs.public.encoded, Base64.NO_WRAP)
            val encodedUiPrivateKey = Base64.encodeToString(keyPairs.private.encoded, Base64.NO_WRAP).trimIndent()

            dataStoreManager.storeValue(DataStoreManager.UI_PUBLIC_KEY, encodedUiPublicKey)
            dataStoreManager.storeValue(DataStoreManager.UI_PRIVATE_KEY, encodedUiPrivateKey)

        } catch (e: Exception) {
            Timber.e("RSA key pair gen exception: ${e.message}")
        }

    }

    fun generatePrivateKeyFromEncodedString(encodedPrivateKeyString: String): PrivateKey? {
        return try {
            val bytes = Base64.decode(encodedPrivateKeyString, Base64.NO_WRAP)
            KeyFactory.getInstance("RSA").generatePrivate(PKCS8EncodedKeySpec(bytes))

        } catch (e: Exception) {
            Timber.e("Extracting Private Key From Encoded String Exception: ${e.message}")
            null
        }

    }

    fun generatePublicKeyFromEncodedString(encodedPublicKeyString: String): PublicKey? {
        return try {
            val bytes = Base64.decode(encodedPublicKeyString, Base64.NO_WRAP)
            KeyFactory.getInstance("RSA").generatePublic(X509EncodedKeySpec(bytes))
        } catch (e: Exception) {
            Timber.e("Extracting Public Key From Encoded String Exception: ${e.message}")
            null
        }
    }

    suspend fun performKeyExchange(payload: Payload): NetworkResponse<KeyExchangeResponse, DefaultErrorResponse> {
        return apiService.exchangeKeys(payload)
    }
}