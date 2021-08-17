package nl.spaan.personeels_app.controller;


import nl.spaan.personeels_app.payload.request.LoginRequest;
import nl.spaan.personeels_app.payload.request.SignupRequest;
import nl.spaan.personeels_app.payload.response.JwtResponse;
import nl.spaan.personeels_app.payload.response.MessageResponse;
import nl.spaan.personeels_app.service.AuthorizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3_600)
@RestController
@RequestMapping("/api/auth")
public class AuthenticatedController {

    AuthorizationService authorizationService;

    @Autowired
    public void setAuthorizationService(AuthorizationService authorizationService) {
        this.authorizationService = authorizationService;
    }

    @PostMapping("/signup")
    public ResponseEntity<MessageResponse> registerUser(@RequestBody SignupRequest signUpRequest) {
        System.out.println("bla signup");
        return authorizationService.registerUser(signUpRequest);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtResponse> authenticateUser(@RequestBody LoginRequest loginRequest) {
        return authorizationService.authenticateUser(loginRequest);
    }
}