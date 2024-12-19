package ru.mtuci.license_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mtuci.license_service.models.orm.License;
import ru.mtuci.license_service.models.orm.UserDetailsImpl;

import java.util.Optional;
import java.util.List;

public interface LicenseRepository extends JpaRepository<License, Long> {
    Optional<License> findByCodeAndUser(String code, UserDetailsImpl user);
    List<License> findByUser(UserDetailsImpl user);
}
