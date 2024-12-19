package ru.mtuci.license_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.mtuci.license_service.models.orm.LicenseType;

import java.util.List;
import java.util.Optional;

public interface LicenseTypeRepository extends JpaRepository<LicenseType, Long> {
    Optional<LicenseType> findByName(String name);

    @Query(value = "SELECT * FROM license_type")
    List<LicenseType> findAllLicenseTypes();
}