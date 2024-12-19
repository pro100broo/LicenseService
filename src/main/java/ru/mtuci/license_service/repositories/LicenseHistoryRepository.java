package ru.mtuci.license_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mtuci.license_service.models.orm.License;
import ru.mtuci.license_service.models.orm.LicenseHistory;
import ru.mtuci.license_service.models.orm.UserDetailsImpl;

import java.util.Optional;

public interface LicenseHistoryRepository extends JpaRepository<LicenseHistory, Long> {
    Optional<LicenseHistory> findByLicenseAndUser(License license, UserDetailsImpl user);
}
