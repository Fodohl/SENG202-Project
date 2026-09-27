package seng202.team1.endpoints.api.v1;


import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import seng202.team1.api.AuthApi;
import seng202.team1.auth.TokenService;
import seng202.team1.taserver.model.Token;

import java.time.OffsetDateTime;

@RestController
public class Auth implements AuthApi {

    private TokenService tokenService;

    public Auth(TokenService tokenService){
        this.tokenService = tokenService;
    }

    @Override
    public ResponseEntity<Token> logUserIn(Object object) {

        Token response = new Token();
        response.token(tokenService.signToken(3345));
        response.expires(OffsetDateTime.now());

        return ResponseEntity.ok(response);

    }

    @Override
    public ResponseEntity<Object> resetUserPassword(Object body) {
        return null;
    }

    @Override
    public ResponseEntity<Token> rotateUserAPIToken() {
        return null;
    }
}
