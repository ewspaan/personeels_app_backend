package nl.spaan.personeels_app.service;

import nl.spaan.personeels_app.model.Company;
import nl.spaan.personeels_app.model.ERole;
import nl.spaan.personeels_app.model.Role;
import nl.spaan.personeels_app.model.User;
import nl.spaan.personeels_app.payload.request.LoginRequest;
import nl.spaan.personeels_app.payload.request.SignupRequest;
import nl.spaan.personeels_app.payload.response.JwtResponse;
import nl.spaan.personeels_app.payload.response.MessageResponse;
import nl.spaan.personeels_app.repository.CompanyRepository;
import nl.spaan.personeels_app.repository.RoleRepository;
import nl.spaan.personeels_app.repository.UserRepository;
import nl.spaan.personeels_app.service.security.jwt.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.HashSet;
import java.util.Set;

@Service
public class AuthorizationService {

    private static final String ROLE_NOT_FOUND_ERROR = "Error: Role is not found.";

    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private CompanyRepository companyRepository;

    private PasswordEncoder encoder;
    private AuthenticationManager authenticationManager;
    private JwtUtils jwtUtils;


    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Autowired
    public void setCompanyRepository(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }

    @Autowired
    public void setEncoder(PasswordEncoder passwordEncoder) {
        this.encoder = passwordEncoder;
    }

    @Autowired
    public void setAuthenticationManager(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Autowired
    public void setJwtUtils(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    public ResponseEntity<MessageResponse> registerUser(@Valid SignupRequest signupRequest) {
        if (Boolean.TRUE.equals(userRepository.existsByUsername(signupRequest.getUsername()))) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is al in gebruik"));
        }
        if (!checkPasswordIsEqual(signupRequest.getPassword(), signupRequest.getPasswordRepeat())) {
            System.out.println("bla password vals");
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Passwords are not equal"));
        }
        System.out.println("bla nieuwe user");
        // Create new user and company
        User user = new User(signupRequest.getUsername(), encoder.encode(signupRequest.getPassword()),signupRequest.getFirstName(),signupRequest.getLastName(),signupRequest.getEmail());
        Company company = new Company();
        user.setCompany(company);

        Set<Role> roles = new HashSet<>();
        Role modRole = roleRepository.findByName(ERole.ROLE_MODERATOR).orElseThrow(() -> new RuntimeException(ROLE_NOT_FOUND_ERROR));
        roles.add(modRole);
        user.setRoles(roles);

        company.setCompanyName(signupRequest.getCompanyName());
        companyRepository.save(company);
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("gelukt"));
    }



    /*
    Bij inloggen
     Authenticate user bij username
    */

    public ResponseEntity<JwtResponse> authenticateUser(LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(),
                        loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        return ResponseEntity.ok(new JwtResponse(jwt));

    }

    public boolean checkPasswordIsEqual(String password, String passwordRepeat) {
        return password.equals(passwordRepeat);
    }
}
