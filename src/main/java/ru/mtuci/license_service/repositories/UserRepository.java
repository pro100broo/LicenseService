package ru.mtuci.license_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.mtuci.license_service.models.orm.UserDetailsImpl;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserDetailsImpl, Long> {
    Optional<UserDetailsImpl> getByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
