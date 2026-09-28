package seng202.team1.endpoints.api.v1;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import seng202.team1.api.UserApi;
import seng202.team1.auth.PasswordHasher;
import seng202.team1.database.exceptions.DuplicateEntryException;
import seng202.team1.taserver.model.UserResponse;
import seng202.team1.types.user.User;
import seng202.team1.common.DateHelpers.*;

import java.time.OffsetDateTime;

import static seng202.team1.TAServer.userTable;
import static seng202.team1.common.DateHelpers.toOffsetTime;

@RestController
public class UserRoutes implements UserApi {


    @Override
    public ResponseEntity<UserResponse> addNewUser(UserResponse userResponse) {

        User user = new User( null, null, null, userResponse.getName(), userResponse.getEmail(), PasswordHasher.hash(userResponse.getPasswordHash()), null);

        try {

            int userID = userTable.add(user);

            if(userID == -1)
                throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to save user to database");

            return ResponseEntity.ok(userResponse);

        } catch (DuplicateEntryException e) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "User with email already exists", e);
        }

    }

    @Override
    public ResponseEntity<UserResponse> getUserDetails() {

        // Get the user
        Long userId = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userTable.getOne(Math.toIntExact(userId));

        if(user == null)
           throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "User no longer exists");

        UserResponse response = new UserResponse();
        response.id(user.id());
        response.created(toOffsetTime(user.created()));
        response.updated(toOffsetTime(user.updated()));

        response.name(user.name());
        response.email(user.email());

        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity<Object> removeUser() {
        return null;
    }

    @Override
    public ResponseEntity<Void> updateUserDetails(UserResponse userResponse) {
        return null;
    }
}
