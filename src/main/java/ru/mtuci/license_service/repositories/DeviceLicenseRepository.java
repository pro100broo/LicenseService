package ru.mtuci.license_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mtuci.license_service.models.orm.DeviceLicense;

public interface DeviceLicenseRepository extends JpaRepository<DeviceLicense, Long> {

}