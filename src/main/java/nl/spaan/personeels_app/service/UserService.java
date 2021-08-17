package nl.spaan.personeels_app.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import nl.spaan.personeels_app.model.User;
import nl.spaan.personeels_app.payload.request.SignupRequest;
import nl.spaan.personeels_app.payload.response.UserResponse;
import nl.spaan.personeels_app.repository.RoleRepository;
import nl.spaan.personeels_app.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.xml.bind.DatatypeConverter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {


    private static final String PREFIX = "Bearer ";
    private static final String ROLE_NOT_FOUND_ERROR = "Role not found";

    @Value("${spaan.sec.jwtSecret}")
    private String jwtSecret;

    private UserRepository userRepository;

    private RoleRepository roleRepository;

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

    public ResponseEntity<?> getUser(String authorization) {
        return ResponseEntity.ok(createUserResponse(getUserFromToken(authorization)));
    }

    public ResponseEntity<?> addUser(String token, SignupRequest signupRequest) {

        User newUser = AuthorizationService.createUser(signupRequest);

        return null;
    }

    private String getUsernameFromToken(String token) {
        String tokenWithoutBearer = removePrefix(token);

        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(jwtSecret))
                .parseClaimsJws(tokenWithoutBearer).getBody();

        return claims.getSubject();
    }

    private User getUserFromToken(String token) {
        String tokenWithoutBearer = removePrefix(token);

        Claims claims = Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(jwtSecret))
                .parseClaimsJws(tokenWithoutBearer).getBody();
        User user = userRepository.findByUsername(claims.getSubject()).get();

        return user;
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
