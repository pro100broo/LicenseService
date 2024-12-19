package ru.mtuci.license_service.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import ru.mtuci.license_service.models.orm.UserDetailsImpl;
import ru.mtuci.license_service.servicies.UserService;


@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final UserService userService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public String signUp(UserDetailsImpl user) throws Exception {
        userService.createUser(user);

        return jwtService.generateToken(user);
    }

    public String signIn(UserDetailsImpl user, String password) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                user.getUsername(),
                password
        ));

        return jwtService.generateToken(user);
    }
}