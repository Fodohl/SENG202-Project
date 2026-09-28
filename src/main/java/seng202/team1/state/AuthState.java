package seng202.team1.state;

import org.apache.commons.validator.routines.EmailValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import seng202.team1.api.tasever.TAServerClient;
import seng202.team1.taserver.api.UserApi;
import seng202.team1.types.User;
import seng202.team1.gui.pages.Pages;
import seng202.team1.gui.phases.Phases;

import java.security.InvalidParameterException;

public class AuthState {

    private static final Logger log = LoggerFactory.getLogger(AuthState.class);

    private User userDetails;
    private String authToken;

    private GlobalState state;

    private TAServerClient server;

    public AuthState(GlobalState state){
        this.state = state;
        this.server = state.getTaServer();
    }

    public User getUserDetails() {
        return userDetails;
    }

    public void setUserDetails(User userDetails) {
        this.userDetails = userDetails;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public void login(String email, String password){

        server.postLogin(email, password, r -> {
            System.out.println(r);
        }).join();

        // Successful login
        state.getRouter().switchPhase(Phases.MAIN);
    }

    public void signUp(User userDetails, String password){

        // Create a new user
        server.postUser(userDetails.email(), userDetails.name(), password, r -> {
            System.out.println(r);
        }).join();


        // Log into the account
        login(userDetails.email(), password);

    }

    public void validateEmail(String enteredEmail) {

        if(!EmailValidator.getInstance().isValid(enteredEmail))
            throw new InvalidParameterException("Email is invalid");


    }

    public void resetPassword(String enteredEmail, String enteredCode, String enteredPassword) {


        // User can now login
        state.getRouter().switchPage(Pages.LOGIN);

    }

    public void validatePassword(String enteredPassword) {

        if(enteredPassword.length() <= 7)
            throw  new InvalidParameterException("Password must be at least 8 characters");

        boolean hasUpperCase = false;
        boolean hasSpecial = false;
        boolean hasNumber = false;

        for(char car : enteredPassword.toCharArray()){

            if (Character.isUpperCase(car))
                hasUpperCase = true;

            if (Character.isDigit(car))
                hasNumber = true;

            if (!Character.isWhitespace(car) && !Character.isLetterOrDigit(car))
                hasSpecial = true;

        }

        if(!hasUpperCase)
            throw new InvalidParameterException("Password must contain at least one uppercase letter");

        if(!hasSpecial)
            throw new InvalidParameterException("Password must contain at least one symbol");

        if(!hasNumber)
            throw new InvalidParameterException("Password must contain at least one number");
    }

    public void validateName(String enteredName) {

        if(enteredName.length() < 3)
            throw  new InvalidParameterException("Name must be 3 or more characters");

        if(enteredName.length() > 15)
            throw  new InvalidParameterException("Name must be 15 or less characters");

    }

    public Integer validateCode(String enteredCode) {

        if(enteredCode.length() != 6)
            throw new InvalidParameterException("Code must e 6 digits");

        try{
            Integer code = Integer.parseInt(enteredCode);
            return code;

        } catch (NumberFormatException e) {
            throw new InvalidParameterException("Code must be a valid number");
        }
    }
}
