package seng202.team1.endpoints.api.v1;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import seng202.team1.api.UserApi;
import seng202.team1.auth.PasswordHasher;
import seng202.team1.database.exceptions.DuplicateEntryException;
import seng202.team1.taserver.model.UserResponse;
import seng202.team1.types.user.User;

import static seng202.team1.TAServer.userTable;

@RestController
public class UserRoutes implements UserApi {

    @Override
    public ResponseEntity<UserResponse> addNewUser(UserResponse userResponse) {

        //@todo validate input
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
        return null;
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
