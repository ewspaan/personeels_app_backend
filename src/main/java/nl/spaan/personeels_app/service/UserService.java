package nl.spaan.personeels_app.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import nl.spaan.personeels_app.model.*;
import nl.spaan.personeels_app.payload.request.SignupRequest;
import nl.spaan.personeels_app.payload.response.MessageResponse;
import nl.spaan.personeels_app.payload.response.UserResponse;
import nl.spaan.personeels_app.repository.FunctionRepository;
import nl.spaan.personeels_app.repository.RoleRepository;
import nl.spaan.personeels_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {


    private static final String PREFIX = "Bearer ";
    private static final String ROLE_NOT_FOUND_ERROR = "Role not found";

    @Value("${spaan.sec.jwtSecret}")
    private String jwtSecret;

    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private FunctionRepository functionRepository;

    private PasswordEncoder encoder;

    @Autowired
    public void setUserRepository(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Autowired
    public void setEncoder(PasswordEncoder encoder) {
        this.encoder = encoder;
    }

    @Autowired
    public void setRoleRepository(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Autowired
    public void setFunctionRepository(FunctionRepository functionRepository) {
        this.functionRepository = functionRepository;
    }

    public ResponseEntity<?> getUser(String authorization) {
        return ResponseEntity.ok(createUserResponse(getUserFromToken(authorization)));
    }

    public ResponseEntity<?> addUser(String token, SignupRequest signupRequest) {

        if (Boolean.TRUE.equals(userRepository.existsByUsername(signupRequest.getUsername()))) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: Username is al in gebruik"));
        }

        User newUser = new User(signupRequest.getUsername(), encoder.encode(signupRequest.getPassword()),signupRequest.getFirstName(),signupRequest.getLastName(),signupRequest.getEmail());
        newUser.setPhoneNumber("06-123456");
        newUser.setDateOfBirth("01-02-2000");
        newUser.setCompany(getUserFromToken(token).getCompany());
        Set<Role> roles = new HashSet<>();
        Role modRole = roleRepository.findByName(ERole.ROLE_USER).orElseThrow(() -> new RuntimeException(ROLE_NOT_FOUND_ERROR));
        roles.add(modRole);
        newUser.setRoles(roles);
        Set<Function> function = new HashSet<>();
        Function modFunction = functionRepository.findByName(EFunction.FUNCTION_COOK).orElseThrow(() -> new RuntimeException(ROLE_NOT_FOUND_ERROR));
        function.add(modFunction);
        newUser.setFunction(function);
        userRepository.save(newUser);
        return null;
    }

    public ResponseEntity<?> getAllEmployees(String token) {

        User user = getUserFromToken(token);
        List<User> users = userRepository.findAllByCompanyId(user.getCompany().getId());

        if(users.size() == 1) {
            return ResponseEntity.badRequest().body(new MessageResponse("Geen huisgenoten gevonden"));
        }
        List<UserResponse> employees = new ArrayList<>();
        for (User value : users) {
            UserResponse userResponse = createUserResponse(value);
            employees.add(userResponse);
        }
        return ResponseEntity.ok(employees);
    }

    private User getUsernameFromToken(String token) {
        String tokenWithoutBearer = removePrefix(token);

        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(jwtSecret))
                .parseClaimsJws(tokenWithoutBearer).getBody();
        User user = userRepository.findByUsername(claims.getSubject()).get();

        return user;
    }

    public User getUserFromToken(String token) {

        return getUsernameFromToken(token);
    }

    private String removePrefix(String token) {
        return token.replace(PREFIX, "");
    }

    private UserResponse createUserResponse(User user){

        List<String> roles = user.getRoles().stream()
                .map(role -> (role.getName().name()))
                .collect(Collectors.toList());
        String role = roles.get(0);
        UserResponse userResponse = new UserResponse();
        userResponse.setId(user.getId());
        userResponse.setUsername(user.getUsername());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setEmail(user.getEmail());
        userResponse.setTelephoneNumber(user.getPhoneNumber());
        userResponse.setDateOfBirth(user.getDateOfBirth());
        userResponse.setRoles(role);
        userResponse.setCompanyName(user.getCompany().getCompanyName());
        return userResponse;
    }



}
