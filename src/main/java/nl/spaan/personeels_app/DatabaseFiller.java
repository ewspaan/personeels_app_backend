package nl.spaan.personeels_app;

import nl.spaan.personeels_app.payload.request.SignupRequest;
import nl.spaan.personeels_app.service.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DatabaseFiller implements CommandLineRunner {

    private final AuthorizationService authorizationService;

    @Autowired
    public DatabaseFiller(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @Override
    public void run(String... args){

        SignupRequest erwin = new SignupRequest();
        erwin.setUsername("ews");
        erwin.setPassword("password");
        erwin.setPasswordRepeat("password");
        erwin.setFirstName("Bla");
        erwin.setLastName("Blatenstein");
        erwin.setEmail("kansloos@gmail.com");
        erwin.setCompanyName("Bla International");

        authorizationService.registerUser(erwin);

    }
}
