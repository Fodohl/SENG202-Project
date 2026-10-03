package seng202.team1.unittests.api.trashdb;

import org.junit.jupiter.api.Test;
import seng202.team1.api.trashdb.PasswordHasher;
import seng202.team1.api.trashdb.TokenService;
import seng202.team1.unittests.TrashAppTest;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;


import static org.assertj.core.api.Fail.fail;

class TokenServiceTest extends TrashAppTest {

    private static TokenService tokenService = new TokenService();

    @Test
    void signShouldParseExactly() {
        assertEquals(255, tokenService.parseToken(tokenService.signToken(255)));
    }

   @Test
    void signShouldHaveAllParts() {
        assertEquals(3, tokenService.signToken(255).split("::").length);
    }

      @Test
    void signShouldSetExpiryInTheFuture() {
        long expires = Long.parseLong(tokenService.signToken(255).split("::")[1]);
        assertTrue(expires  > Instant.now().getEpochSecond());
    }

      @Test
    void alteredTokenIDShouldBeRejected() {
        String[] parts = tokenService.signToken(255).split("::");
        assertEquals(-1, tokenService.parseToken("2048::" + parts[1] + "::" + parts[2]));
    }

      @Test
    void alteredTokenHashShouldBeRejected() {
        String[] parts = tokenService.signToken(255).split("::");
        assertEquals(-1, tokenService.parseToken("255::" + parts[1] + "::6D617877617368657265"));
    }

      @Test
    void malformedShouldBeRejected() {
        assertEquals(-1, tokenService.parseToken("::__::"));
    }

       @Test
    void expiredShouldBeRejected() {
         String payload = "255::" + (Instant.now().getEpochSecond() - 10);
        assertEquals(-1, tokenService.parseToken(payload + "::" + tokenService.sign(payload)));
    }
}