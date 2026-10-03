package seng202.team1.unittests.api.trashdb;

import org.junit.jupiter.api.Test;
import seng202.team1.api.trashdb.PasswordHasher;
import seng202.team1.unittests.TrashAppTest;

import static org.assertj.core.api.Fail.fail;

class TokenServiceTest extends TrashAppTest {

    @Test
    void signShouldParseExactly() {
        fail("todo");
    }

      @Test
    void alteredTokenIDShouldBeRejected() {
        fail("todo");
    }

      @Test
    void alteredTokenHashShouldBeRejected() {
        fail("todo");
    }

      @Test
    void malformedShouldBeRejected() {
        fail("todo");
    }

       @Test
    void expiredShouldBeRejected() {
        fail("todo");
    }

}