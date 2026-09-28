package seng202.team1.unittests.data;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import seng202.team1.data.sqlite.PasswordHasher;

/**
 * Tests for PasswordHasher (no database needed)
 * @author abdallaweza
 */
public class PasswordHasherTest {

    @Test
    void testCorrectPasswordVerifies() {
        String stored = PasswordHasher.hash("Password1!");
        Assertions.assertTrue(PasswordHasher.verify("Password1!", stored));
    }

    @Test
    void testWrongPasswordFails() {
        String stored = PasswordHasher.hash("Password1!");
        Assertions.assertFalse(PasswordHasher.verify("password1!", stored));
        Assertions.assertFalse(PasswordHasher.verify("", stored));
    }

    @Test
    void testHashIsNotPlainText() {
        String stored = PasswordHasher.hash("Password1!");
        Assertions.assertFalse(stored.contains("Password1!"));
    }

    @Test
    void testSamePasswordGivesDifferentHashes() {
        // Random salt: identical passwords must not produce identical hashes
        String first = PasswordHasher.hash("Password1!");
        String second = PasswordHasher.hash("Password1!");
        Assertions.assertNotEquals(first, second);
        Assertions.assertTrue(PasswordHasher.verify("Password1!", first));
        Assertions.assertTrue(PasswordHasher.verify("Password1!", second));
    }

    @Test
    void testStoredFormatIsSaltColonHash() {
        String stored = PasswordHasher.hash("Password1!");
        Assertions.assertEquals(2, stored.split(":").length);
    }
}
