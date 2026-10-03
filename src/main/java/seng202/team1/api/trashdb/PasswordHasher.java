package seng202.team1.api.trashdb;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * A backend utility to securely hash and verify plaintext passwords
 */
public class PasswordHasher {

    /// OSWAP recommended hashing iterations for 256bit PBKDF2
    private static final int ITERATIONS = 600_000;

    /// Length of hashed password in bits
    private static final int KEY_BITS = 256;

    /**
     * Hash a given raw password with salt to prevent rainbowtable attacks
     *
     * @param password The plaintext user password
     * @return The hashed password in the form 'salt:salted-hashed-password'
     */
    public static String hash(String password) {

        // Add random string to the password so that the same password doesn't give hash twice
        byte[] salt = new byte[16];
        new SecureRandom().nextBytes(salt);

        // Provide salt and password for later verification
        return b64(salt) + ":" + b64(derive(password, salt));
    }

    /**
     * Check that a plaintext password matches a hashed password
     *
     * @param password The raw password text
     * @param stored The full 'salt:salted-hashed-password' password hash
     * @return True if the password matches, false otherwise
     */
    public static boolean verify(String password, String stored) {

        // Get the random part of hash to add to the tested password
        String[] parts = stored.split(":");
        byte[] salt = Base64.getDecoder().decode(parts[0]);

        // If the password is the same it should exactly equal test password + salt used
        byte[] expected = Base64.getDecoder().decode(parts[1]);
        return MessageDigest.isEqual(expected, derive(password, salt));
    }

    /**
     *
     *
     * @param password
     * @param salt
     * @return
     */
    private static byte[] derive(String password, byte[] salt) {
        try {

            PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, KEY_BITS);
            return SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256").generateSecret(spec).getEncoded();

        } catch (Exception e) {
            throw new IllegalStateException("Hashing failed", e);
        }
    }

    /**
     * Convert a buffer into a base64 encoded string
     *
     * @param b The buffer of bytes to convert
     * @return The encoded string
     */
    private static String b64(byte[] b) {
        return Base64.getEncoder().encodeToString(b);
    }
}
