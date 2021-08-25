package nl.spaan.personeels_app.controller;

import nl.spaan.personeels_app.payload.request.SignupRequest;
import nl.spaan.personeels_app.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Map;


@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(value = "/api/users")
public class UserController {


    private UserService userService;

    @Autowired
    public void setUserService(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/download")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('USER')")
    public ResponseEntity<?> getUser(@RequestHeader Map<String, String> headers) {
        return userService.getUser(headers.get("authorization"));
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('MODERATOR')")
    public ResponseEntity<?> addUser(@RequestHeader Map<String, String> headers,
                                     @RequestBody SignupRequest signupRequest) {
        return userService.addUser(headers.get("authorization"),signupRequest);
    }

    @GetMapping("/all")
    @PreAuthorize("hasRole('MODERATOR') or hasRole('USER')")
    public ResponseEntity<?> getAllRoommates(@RequestHeader Map<String, String> headers){
        return userService.getAllEmployees(headers.get("authorization"));
    }
}
