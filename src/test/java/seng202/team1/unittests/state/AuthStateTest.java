package seng202.team1.unittests.state;

import org.junit.jupiter.api.Test;
import seng202.team1.unittests.TrashAppTest;

import static org.assertj.core.api.Fail.fail;

public class AuthStateTest extends TrashAppTest {

    @Test
    void passwordShouldErrorIfShorterThan8Characters() {
        fail("todo");

    }

    @Test
    void passwordShouldErrorIfNoUppercase() {
        fail("todo");

    }

    @Test
    void passwordShouldErrorIfNoSymbol() {
        fail("todo");

    }

    @Test
    void passwordShouldErrorIfNoNumber() {
        fail("todo");

    }

    @Test
    void passwordShouldNotErrorIfAllRulesMet() {
        fail("todo");

    }

    @Test
    void nameShouldErrorIfShorterThan3Characters() {
        fail("todo");

    }

    @Test
    void nameShouldErrorIfLongerThan15Characters() {
        fail("todo");

    }

    @Test
    void emailShouldErrorIfNoAtSymbol() {
        fail("todo");

    }

    @Test
    void emailShouldErrorIfNoDomain() {
        fail("todo");

    }

    @Test
    void emailShouldErrorIfNoNamePart() {
        fail("todo");

    }

    @Test
    void emailShouldNotErrorIfAllRulesMet() {
        fail("todo");

    }

    @Test
    void codeShouldErrorIfShorterThan6Characters() {
        fail("todo");

    }


    @Test
    void codeShouldErrorIfNotNumeric() {
        fail("todo");

    }

    @Test
    void codeShouldReturnParsedIntegerIfValid() {
        fail("todo");

    }


    @Test
    void setSessionShouldPropagateToServerClient() {
        fail("todo");

    }

    @Test
    void updateUserShouldReplaceNameAndEmailOnSession() {
        fail("todo");

    }

    @Test
    void updateUserShouldKeepIdAndCreatedDate() {
        fail("todo");

    }

    @Test
    void updateUserShouldBumpUpdatedTimestamp() {
        fail("todo");

    }
}
