package seng202.team1.endpoints.api.v1;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import seng202.team1.api.AuthApi;
import seng202.team1.auth.PasswordHasher;
import seng202.team1.auth.TokenService;
import seng202.team1.taserver.model.LogUserInRequest;
import seng202.team1.taserver.model.TokenResponse;
import seng202.team1.types.user.User;

import java.time.OffsetDateTime;

import static seng202.team1.TAServer.userTable;

@RestController
public class AuthRoutes implements AuthApi {

    private TokenService tokenService;

    public AuthRoutes(TokenService tokenService){
        this.tokenService = tokenService;
    }

    @Override
    public ResponseEntity<TokenResponse> logUserIn(LogUserInRequest logUserInRequest) {

        // User with that email must exist
        User user = userTable.getByEmail(logUserInRequest.getEmail());
        if(user == null)
           throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");

        if(!PasswordHasher.verify(logUserInRequest.getPassword(), user.passwordHash()))
               throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Invalid email or password");

        // Authenticate the user
        TokenResponse response = new TokenResponse();
        response.token(tokenService.signToken(user.id()));
        response.expires(OffsetDateTime.now());

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Object> resetUserPassword(Object body) {
        return null;
    }

    @Override
    public ResponseEntity<TokenResponse> rotateUserAPIToken() {
        return null;
    }
}
