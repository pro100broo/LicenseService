package ru.mtuci.license_service.servicies;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mtuci.license_service.models.orm.Device;
import ru.mtuci.license_service.models.orm.UserDetailsImpl;
import ru.mtuci.license_service.models.rest.request.CreateDevice;
import ru.mtuci.license_service.models.rest.request.UpdateDevice;
import ru.mtuci.license_service.repositories.DeviceRepository;
import ru.mtuci.license_service.utils.LicenseServiceException;

import java.util.List;
import java.util.Optional;

//TODO: 1. updateDevice - для чего менять пользователя?

@Service
@RequiredArgsConstructor
public class DeviceService {
    private final DeviceRepository repository;

    public Device createDevice(UserDetailsImpl user, CreateDevice device) throws Exception {
        String deviceName = device.getName();
        String deviceMacAddress = device.getMacAddress();

        if (repository.findByUserAndMacAddressAndName(user, deviceMacAddress, deviceName).isPresent()) {
            throw new LicenseServiceException("Device already exists");
        }

        Device newDevice = new Device();

        newDevice.setName(deviceName);
        newDevice.setMacAddress(deviceMacAddress);
        newDevice.setUser(user);

        Device createdDevice = repository.save(newDevice);
        repository.flush();

        return createdDevice;
    }

    public Device updateDevice(UserDetailsImpl user, UpdateDevice device) throws Exception {
        Long deviceId = device.getDeviceId();
        Optional<Device> foundDevice = repository.findByIdAndUser(deviceId, user);

        if (foundDevice.isEmpty()) {
            throw new LicenseServiceException(String.format("Device with id %d not found", deviceId));
        }

        Device newDevice = foundDevice.get();

        newDevice.setName(device.getName());
        newDevice.setMacAddress(device.getMacAddress());
        newDevice.setUser(user);

        Device updatedDevice = repository.save(newDevice);
        repository.flush();

        return updatedDevice;
    }


    public List<Device> getDevices(UserDetailsImpl user) {
        return repository.findByUser(user);
    }



    public void deleteDevice(Long deviceId, UserDetailsImpl user) throws Exception {

        Optional<Device> device = repository.findByIdAndUser(deviceId, user);

        if (device.isEmpty()) {
            throw new LicenseServiceException(String.format("Device with id %d not found", deviceId));
        }

        repository.delete(device.get());
    }
}
