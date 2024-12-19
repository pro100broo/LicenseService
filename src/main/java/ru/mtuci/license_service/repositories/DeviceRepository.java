package ru.mtuci.license_service.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.mtuci.license_service.models.orm.Device;
import ru.mtuci.license_service.models.orm.UserDetailsImpl;


import java.util.List;
import java.util.Optional;

public interface DeviceRepository extends JpaRepository<Device, Long> {
    Optional<Device> findByIdAndUser(Long id, UserDetailsImpl user);
    Optional<Device> findByUserAndMacAddressAndName(UserDetailsImpl user, String mac_address, String name);
    List<Device> findByUser(UserDetailsImpl user);
}
