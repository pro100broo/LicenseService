package ru.mtuci.license_service.servicies;

import lombok.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import ru.mtuci.license_service.models.orm.UserDetailsImpl;
import ru.mtuci.license_service.repositories.UserRepository;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository repository;

    public void createUser(UserDetailsImpl user) throws Exception {

        if (checkByEmail(user.getEmail())) {
            throw new Exception("Specified email is already in use");
        }

        if (checkByUsername(user.getUsername())) {
            throw new Exception("Specified username is already in use");
        }

        repository.save(user);
    }

    public UserDetailsImpl getByUsername(String username) {
        return repository.getByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Username not found"));
    }

    public boolean checkByUsername(String username) {
        return repository.existsByUsername(username);
    }

    public boolean checkByEmail(String email) {
        return repository.existsByEmail(email);
    }

    public UserDetailsService userDetailsService() {
        return this::getByUsername;
    }

    public UserDetailsImpl getCurrentUser() {
        var username = SecurityContextHolder.getContext().getAuthentication().getName();
        return getByUsername(username);
    }

}