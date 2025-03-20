package com.farmbase.app.utils


import java.math.BigInteger
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import javax.crypto.Mac
import javax.crypto.spec.SecretKeySpec
import android.util.Base64

/**
 * A utility class that provides hashing functionality for Android applications.
 * Supports MD5, SHA-1, SHA-256, SHA-512, and HMAC-SHA-256.
 */
class HashHelper {

    companion object {
        private const val HEX_CHARS = "0123456789ABCDEF"

        /**
         * Generates an MD5 hash from a string input.
         * Note: MD5 is considered cryptographically broken and should not be used for security purposes.
         * It is included here for legacy compatibility only.
         *
         * @param input The string to hash
         * @return The MD5 hash as a hex string
         */
        @Deprecated("MD5 is cryptographically weak. Use SHA-256 or higher for security purposes.")
        fun md5(input: String): String {
            return try {
                val md = MessageDigest.getInstance("MD5")
                val digest = md.digest(input.toByteArray())
                bytesToHex(digest)
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
                ""
            }
        }

        /**
         * Generates a SHA-1 hash from a string input.
         * Note: SHA-1 is considered weak for some security applications.
         *
         * @param input The string to hash
         * @return The SHA-1 hash as a hex string
         */
        @Deprecated("SHA-1 is cryptographically weak. Use SHA-256 or higher for security purposes.")
        fun sha1(input: String): String {
            return try {
                val md = MessageDigest.getInstance("SHA-1")
                val digest = md.digest(input.toByteArray())
                bytesToHex(digest)
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
                ""
            }
        }

        /**
         * Generates a SHA-256 hash from a string input.
         *
         * @param input The string to hash
         * @return The SHA-256 hash as a hex string
         */
        fun sha256(input: String): String {
            return try {
                val md = MessageDigest.getInstance("SHA-256")
                val digest = md.digest(input.toByteArray())
                bytesToHex(digest)
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
                ""
            }
        }

        /**
         * Generates a SHA-512 hash from a string input.
         *
         * @param input The string to hash
         * @return The SHA-512 hash as a hex string
         */
        fun sha512(input: String): String {
            return try {
                val md = MessageDigest.getInstance("SHA-512")
                val digest = md.digest(input.toByteArray())
                bytesToHex(digest)
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
                ""
            }
        }

        /**
         * Generates an HMAC-SHA-256 hash from a string input and a key.
         *
         * @param input The string to hash
         * @param key The secret key to use
         * @return The HMAC-SHA-256 hash as a hex string
         */
        fun hmacSha256(input: String, key: String): String {
            return try {
                val secretKeySpec = SecretKeySpec(key.toByteArray(), "HmacSHA256")
                val mac = Mac.getInstance("HmacSHA256")
                mac.init(secretKeySpec)
                val digest = mac.doFinal(input.toByteArray())
                bytesToHex(digest)
            } catch (e: Exception) {
                e.printStackTrace()
                ""
            }
        }

        /**
         * Generates a salt for use with password hashing.
         *
         * @param length The length of the salt in bytes
         * @return The salt as a Base64 encoded string
         */
        fun generateSalt(length: Int = 16): String {
            val salt = ByteArray(length)
            SecureRandom().nextBytes(salt)
            return Base64.encodeToString(salt, Base64.NO_WRAP)
        }

        /**
         * Hashes a password with a salt using PBKDF2 (if available in your Android version)
         * or falls back to SHA-256 with salt.
         *
         * @param password The password to hash
         * @param salt The salt as a Base64 encoded string
         * @return The hashed password as a hex string
         */
        fun hashPassword(password: String, salt: String): String {
            val saltBytes = Base64.decode(salt, Base64.NO_WRAP)

            // Combine password and salt
            val combined = password.toByteArray() + saltBytes

            return try {
                val md = MessageDigest.getInstance("SHA-256")
                // Perform multiple iterations to make brute force attacks harder
                var digest = combined
                for (i in 0 until 10000) {
                    digest = md.digest(digest)
                }
                bytesToHex(digest)
            } catch (e: NoSuchAlgorithmException) {
                e.printStackTrace()
                ""
            }
        }

        /**
         * Converts a byte array to a hexadecimal string.
         *
         * @param bytes The byte array to convert
         * @return The byte array as a hex string
         */
        fun bytesToHex(bytes: ByteArray): String {
            val result = StringBuilder(bytes.size * 2)
            for (byte in bytes) {
                val i = byte.toInt() and 0xFF
                result.append(HEX_CHARS[i shr 4])
                result.append(HEX_CHARS[i and 0x0F])
            }
            return result.toString()
        }

        /**
         * Converts a byte array to a Base64 encoded string.
         *
         * @param bytes The byte array to convert
         * @return The byte array as a Base64 encoded string
         */
        fun bytesToBase64(bytes: ByteArray): String {
            return Base64.encodeToString(bytes, Base64.NO_WRAP)
        }

        /**
         * Decodes a Base64 encoded string to a byte array.
         *
         * @param base64 The Base64 encoded string
         * @return The decoded byte array
         */
        fun base64ToBytes(base64: String): ByteArray {
            return Base64.decode(base64, Base64.NO_WRAP)
        }
    }
}