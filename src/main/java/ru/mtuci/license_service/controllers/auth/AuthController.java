package ru.mtuci.license_service.controllers.auth;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.mtuci.license_service.models.orm.Role;
import ru.mtuci.license_service.models.orm.UserDetailsImpl;
import ru.mtuci.license_service.models.rest.response.GenericResponse;
import ru.mtuci.license_service.models.rest.security.JwtAuth;
import ru.mtuci.license_service.models.rest.security.SignIn;
import ru.mtuci.license_service.models.rest.security.SignUp;
import ru.mtuci.license_service.security.AuthenticationService;
import ru.mtuci.license_service.servicies.UserService;


@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthenticationService authenticationService;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;


    @PostMapping("/sign-up")
    public ResponseEntity<?> signUp(@RequestBody SignUp request) {
        try {
            var user = UserDetailsImpl.builder()
                    .username(request.getUsername())
                    .email(request.getEmail())
                    .password(passwordEncoder.encode(request.getPassword()))
                    .role(Role.ROLE_USER)
                    .build();

            String token = authenticationService.signUp(user);
            return ResponseEntity.status(HttpStatus.OK).body(new JwtAuth(token));

        } catch (Exception ex) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GenericResponse(500, ex.getMessage()));
        }
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody SignIn request) {
        try {
            UserDetailsImpl user = userService.getByUsername(request.getUsername());

            String token = authenticationService.signIn(user, request.getPassword());
            return ResponseEntity.status(HttpStatus.OK).body(new JwtAuth(token));

        } catch (Exception ex) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GenericResponse(500, ex.getMessage()));
        }
    }
}
