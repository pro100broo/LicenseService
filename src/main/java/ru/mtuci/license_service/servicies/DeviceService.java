package ru.mtuci.license_service.servicies;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mtuci.license_service.models.orm.Device;
import ru.mtuci.license_service.models.orm.DeviceLicense;
import ru.mtuci.license_service.models.orm.License;
import ru.mtuci.license_service.models.orm.UserDetailsImpl;
import ru.mtuci.license_service.models.rest.request.CreateDevice;
import ru.mtuci.license_service.models.rest.request.UpdateDevice;
import ru.mtuci.license_service.repositories.DeviceLicenseRepository;
import ru.mtuci.license_service.repositories.DeviceRepository;
import ru.mtuci.license_service.repositories.LicenseRepository;
import ru.mtuci.license_service.utils.LicenseServiceException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class DeviceService {
    private final LicenseRepository licenseRepository;
    private final DeviceRepository deviceRepository;
    private final DeviceLicenseRepository deviceLicenseRepository;

    private final LicenseService licenseService;

    public Device createDevice(UserDetailsImpl user, CreateDevice device) throws Exception {
        String deviceName = device.getName();
        String deviceMacAddress = device.getMacAddress();

        if (deviceRepository.findByUserAndMacAddressAndName(user, deviceMacAddress, deviceName).isPresent()) {
            throw new LicenseServiceException("Device already exists");
        }

        Device newDevice = new Device();

        newDevice.setName(deviceName);
        newDevice.setMacAddress(deviceMacAddress);
        newDevice.setUser(user);

        Device createdDevice = deviceRepository.save(newDevice);
        deviceRepository.flush();

        return createdDevice;
    }

    public Device updateDevice(UserDetailsImpl user, UpdateDevice device) throws Exception {
        Long deviceId = device.getDeviceId();
        Optional<Device> foundDevice = deviceRepository.findByIdAndUser(deviceId, user);

        if (foundDevice.isEmpty()) {
            throw new LicenseServiceException(String.format("Device with id %d not found", deviceId));
        }

        Device newDevice = foundDevice.get();

        newDevice.setName(device.getName());
        newDevice.setMacAddress(device.getMacAddress());

        Device updatedDevice = deviceRepository.save(newDevice);
        deviceRepository.flush();

        return updatedDevice;
    }


    public List<Device> getDevices(UserDetailsImpl user) {
        return deviceRepository.findByUser(user);
    }



    public void deleteDevice(Long deviceId, UserDetailsImpl user) throws Exception {

        Optional<Device> device = deviceRepository.findByIdAndUser(deviceId, user);

        if (device.isEmpty()) {
            throw new LicenseServiceException(String.format("Device with id %d not found", deviceId));
        }

        Device confirmedDevice = device.get();
        Optional<DeviceLicense> deviceLicense = deviceLicenseRepository.findByDevice(confirmedDevice);

        if (deviceLicense.isPresent()) {
            DeviceLicense confirmedDeviceLicense = deviceLicense.get();
            deviceLicenseRepository.delete(confirmedDeviceLicense);

            License license = confirmedDeviceLicense.getLicense();
            license.setDeviceCount(license.getDeviceCount() - 1);
            licenseRepository.save(license);

            licenseService.updateLicenseHistory("Removed device", user, license);
        }

        deviceRepository.delete(confirmedDevice);
    }
}
