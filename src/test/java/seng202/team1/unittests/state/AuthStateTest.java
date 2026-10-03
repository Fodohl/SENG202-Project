package seng202.team1.unittests.state;

import org.junit.jupiter.api.Test;
import seng202.team1.state.AuthState;
import seng202.team1.types.user.Session;
import seng202.team1.unittests.TrashAppTest;

import java.security.InvalidParameterException;

import static org.assertj.core.api.Fail.fail;

import static org.junit.jupiter.api.Assertions.*;


public class AuthStateTest extends TrashAppTest {

    @Test
    void passwordShouldErrorIfShorterThan8Characters() {
        assertThrows(InvalidParameterException.class, () -> AuthState.validatePassword("abcdefg"));
    }

    @Test
    void passwordShouldErrorIfNoUppercase() {
        assertThrows(InvalidParameterException.class, () -> AuthState.validatePassword("abcdefgh"));
    }

    @Test
    void passwordShouldErrorIfNoSymbol() {
        assertThrows(InvalidParameterException.class, () -> AuthState.validatePassword("Abcdefgh"));
    }

    @Test
    void passwordShouldErrorIfNoNumber() {
        assertThrows(InvalidParameterException.class, () -> AuthState.validatePassword("A&cdefgh"));
    }

    @Test
    void passwordShouldNotErrorIfAllRulesMet() {
        assertDoesNotThrow(() -> AuthState.validatePassword("A&1defgh"));
    }

    @Test
    void nameShouldErrorIfShorterThan3Characters() {
        assertThrows(InvalidParameterException.class, () -> AuthState.validateName("12"));
    }

    @Test
    void nameShouldErrorIfLongerThan15Characters() {
        assertThrows(InvalidParameterException.class, () -> AuthState.validateName("1234567890123456"));
    }

    @Test
    void emailShouldErrorIfNoAtSymbol() {
        assertThrows(InvalidParameterException.class, () -> AuthState.validateEmail("userATtest.com"));
    }

    @Test
    void emailShouldErrorIfNoDomain() {
        assertThrows(InvalidParameterException.class, () -> AuthState.validateEmail("user@test"));
    }

    @Test
    void emailShouldErrorIfNoNamePart() {
        assertThrows(InvalidParameterException.class, () -> AuthState.validateEmail("@test.com"));

    }

    @Test
    void emailShouldNotErrorIfAllRulesMet() {
        assertDoesNotThrow(() -> AuthState.validateEmail("user@test.com"));
    }

    @Test
    void codeShouldErrorIfShorterThan6Characters() {

        assertThrows(InvalidParameterException.class, () -> AuthState.validateCode("abc"));
    }


    @Test
    void codeShouldErrorIfNotNumeric() {
        assertThrows(InvalidParameterException.class, () -> AuthState.validateCode("abcdef"));
    }

    @Test
    void codeShouldReturnParsedIntegerIfValid() {
        assertEquals(123456, AuthState.validateCode("123456"));
    }
}