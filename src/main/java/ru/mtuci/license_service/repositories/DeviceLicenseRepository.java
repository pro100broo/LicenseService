package ru.mtuci.license_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mtuci.license_service.models.orm.Device;
import ru.mtuci.license_service.models.orm.DeviceLicense;

import java.util.Optional;

public interface DeviceLicenseRepository extends JpaRepository<DeviceLicense, Long> {
    Optional<DeviceLicense> findByDevice(Device device);
}