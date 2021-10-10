package nl.spaan.personeels_app;

import nl.spaan.personeels_app.payload.request.LoginRequest;
import nl.spaan.personeels_app.payload.request.SignupRequest;
import nl.spaan.personeels_app.payload.response.MessageResponse;
import nl.spaan.personeels_app.service.AuthorizationService;
import nl.spaan.personeels_app.service.UserService;
import org.aspectj.weaver.patterns.IToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

@Component
public class DatabaseFiller implements CommandLineRunner {

    private final AuthorizationService authorizationService;
    private final UserService userService;

    @Autowired
    public DatabaseFiller(AuthorizationService authorizationService, UserService userService) {
        this.authorizationService = authorizationService;
        this.userService = userService;
    }


    @Override
    public void run(String... args){

        SignupRequest ralph = new SignupRequest();
        ralph.setUsername("rjs");
        ralph.setPassword("password");
        ralph.setPasswordRepeat("password");
        ralph.setFirstName("Blaat");
        ralph.setLastName("Der Blater");
        ralph.setEmail("prullebak@gmail.com");
        ralph.setCompanyName("SPN Windows");

        authorizationService.registerUser(ralph);

        SignupRequest erwin = new SignupRequest();
        erwin.setUsername("ews");
        erwin.setPassword("password");
        erwin.setPasswordRepeat("password");
        erwin.setFirstName("Bla");
        erwin.setLastName("Blatenstein");
        erwin.setEmail("kansloos@gmail.com");
        erwin.setCompanyName("Bla International");


        authorizationService.registerUser(erwin);
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setPassword(erwin.getPassword());
        loginRequest.setUsername(erwin.getUsername());
        String token = authorizationService.authenticateUser(loginRequest).getBody().getAccessToken();

        SignupRequest bla = new SignupRequest();
        bla.setUsername("blaat");
        bla.setPassword("password");
        bla.setPasswordRepeat("password");
        bla.setFirstName("Blaat");
        bla.setLastName("De Vries");
        bla.setEmail("bla@gmail.com");

        userService.addUser(token,bla);

        SignupRequest bla1 = new SignupRequest();
        bla1.setUsername("amateur");
        bla1.setPassword("password");
        bla1.setPasswordRepeat("password");
        bla1.setFirstName("Andre");
        bla1.setLastName("Mateur");
        bla1.setEmail("a.mateur@gmail.com");

        userService.addUser(token,bla1);

        SignupRequest bla2 = new SignupRequest();
        bla2.setUsername("worst");
        bla2.setPassword("password");
        bla2.setPasswordRepeat("password");
        bla2.setFirstName("Hans");
        bla2.setLastName("Worst");
        bla2.setEmail("worst@gmail.com");

        userService.addUser(token,bla2);

    }
}
