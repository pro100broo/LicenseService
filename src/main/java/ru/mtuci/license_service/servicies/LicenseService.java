package ru.mtuci.license_service.servicies;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mtuci.license_service.models.orm.*;
import ru.mtuci.license_service.models.rest.request.ActivateLicense;
import ru.mtuci.license_service.models.rest.request.CreateLicense;
import ru.mtuci.license_service.models.rest.request.UpdateLicense;
import ru.mtuci.license_service.models.rest.response.Ticket;
import ru.mtuci.license_service.repositories.*;
import ru.mtuci.license_service.utils.LicenseServiceException;

import java.util.*;

@Service
@RequiredArgsConstructor
public class LicenseService {
    private final ProductRepository productRepository;
    private final LicenseTypeRepository licenseTypeRepository;
    private final LicenseHistoryRepository licenseHistoryRepository;
    private final LicenseRepository licenseRepository;
    private final DeviceRepository deviceRepository;
    private final DeviceLicenseRepository deviceLicenseRepository;
    private final TicketService ticketService;


    public License createLicense(UserDetailsImpl user, CreateLicense licenseData) throws Exception {
        Optional<Product> product = productRepository.findById(licenseData.getProductId());
        Optional<LicenseType> licenseType = licenseTypeRepository.findById(licenseData.getLicenseTypeId());

        if (product.isEmpty()) {
            throw new LicenseServiceException("Specified product not found");
        }

        if (product.get().isBlocked()) {
            throw new LicenseServiceException("Specified product is blocked. Try another product");
        }

        if (licenseType.isEmpty()) {
            throw new LicenseServiceException("Specified license type not found");
        }

        String code = String.valueOf(UUID.randomUUID());
        Long defaultDeviceCount = 10L;
        String defaultStatus = "Not active";
        String defaultDescription = "Another license";

        LicenseType confirmedLicenseType = licenseType.get();
        License newLicense = new License();

        newLicense.setCode(code);
        newLicense.setProduct(product.get());
        newLicense.setType(confirmedLicenseType);
        newLicense.setBlocked(false);
        newLicense.setDeviceCount(defaultDeviceCount);
        newLicense.setUser(user);
        newLicense.setDuration(confirmedLicenseType.getDefaultDuration());
        newLicense.setDescription(confirmedLicenseType.getDescription());

        License createdLicense = licenseRepository.save(newLicense);
        licenseRepository.flush();

        addLicenseHistory(defaultStatus, defaultDescription, user, newLicense);

        return createdLicense;
    }


    public Ticket activateLicense(UserDetailsImpl user, ActivateLicense licenseData) throws Exception {
        Optional<License> license = licenseRepository.findByCodeAndUser(licenseData.getCode(), user);
        Optional<Device> device = deviceRepository.findByIdAndUser(licenseData.getDeviceId(), user);

        if (license.isEmpty()) {
            throw new LicenseServiceException("Specified license not found");
        }

        if (device.isEmpty()) {
            throw new LicenseServiceException("Specified device not found");
        }

        License confirmedLicense = license.get();
        Device confirmedDevice = device.get();

        if (confirmedLicense.getDeviceCount() >= 10) {
            throw new LicenseServiceException("Devices count exceeded for specified license");
        }

        if (confirmedLicense.isBlocked()) {
            throw new LicenseServiceException("Specified license is blocked. Try another license");
        }

        if (new Date().after(confirmedLicense.getEndingDate())) {
            throw new LicenseServiceException("License has expired. You need to renew it");
        }

        if (confirmedLicense.getEndingDate() != null) {
            confirmedLicense = setLicenseTimestamps(confirmedLicense);
        }

        confirmedLicense.setDeviceCount(confirmedLicense.getDeviceCount() + 1);

        License activatedLicense = licenseRepository.save(confirmedLicense);
        licenseRepository.flush();

        addDeviceLicenseHistory(activatedLicense, confirmedDevice);

        return ticketService.generateTicket(activatedLicense, confirmedDevice, user);

    }

    public License updateLicense(UserDetailsImpl user, UpdateLicense licenseData) throws Exception {
        Optional<License> license = licenseRepository.findByCodeAndUser(licenseData.getCode(), user);
        Optional<Product> product = productRepository.findById(licenseData.getProductId());
        Optional<LicenseType> licenseType = licenseTypeRepository.findById(licenseData.getLicenseTypeId());

        if (license.isEmpty()) {
            throw new LicenseServiceException("Specified license not found");
        }

        if (product.isEmpty()) {
            throw new LicenseServiceException("Specified product not found");
        }

        if (product.get().isBlocked()) {
            throw new LicenseServiceException("Specified product is blocked. Try another product");
        }

        if (licenseType.isEmpty()) {
            throw new LicenseServiceException("Specified license type not found");
        }

        License confirmedLicense = license.get();
        Product confirmedProduct = product.get();
        LicenseType confirmedLicenseType = licenseType.get();

        String defaultDescription = licenseData.getDescription();
        String defaultStatus = "Updated";


        confirmedLicense.setProduct(confirmedProduct);
        confirmedLicense.setType(confirmedLicenseType);
        confirmedLicense.setDescription(defaultDescription);

        licenseRepository.save(confirmedLicense);

        updateLicenseHistory(defaultStatus, user, confirmedLicense);

        return confirmedLicense;
    }

    public Ticket getLicenseInfo(UserDetailsImpl user, ActivateLicense licenseData) throws Exception {
        Optional<License> license = licenseRepository.findByCodeAndUser(licenseData.getCode(), user);
        Optional<Device> device = deviceRepository.findByIdAndUser(licenseData.getDeviceId(), user);

        if (license.isEmpty()) {
            throw new LicenseServiceException("Specified license not found");
        }

        if (device.isEmpty()) {
            throw new LicenseServiceException("Specified device not found");
        }

        License confirmedLicense = license.get();
        Device confirmedDevice = device.get();

        return ticketService.generateTicket(confirmedLicense, confirmedDevice, user);

    }

    public License renewLicense(UserDetailsImpl user, String code) throws Exception {
        Optional<License> license = licenseRepository.findByCodeAndUser(code, user);

        if (license.isEmpty()) {
            throw new LicenseServiceException("Specified license not found");
        }

        License confirmedLicense = license.get();

        if (confirmedLicense.isBlocked()) {
            throw new LicenseServiceException("Specified license is blocked");
        }

        if (new Date().before(confirmedLicense.getEndingDate())) {
            throw new LicenseServiceException("License is already active");
        }

        confirmedLicense = setLicenseTimestamps(confirmedLicense);
        License renewedLicense = licenseRepository.save(confirmedLicense);

        return renewedLicense;
    }

    public List<License> getLicenses(UserDetailsImpl user) {
        return licenseRepository.findByUser(user);
    }

    public void addLicenseHistory(String status, String description, UserDetailsImpl user, License license){
        LicenseHistory newLicenseHistory = new LicenseHistory();

        newLicenseHistory.setLicense(license);
        newLicenseHistory.setStatus(status);
        newLicenseHistory.setChangeDate(new Date());
        newLicenseHistory.setDescription(description);
        newLicenseHistory.setUser(user);

        licenseHistoryRepository.save(newLicenseHistory);
    }

    public void updateLicenseHistory(String status, UserDetailsImpl user, License license){
        LicenseHistory licenseHistory = licenseHistoryRepository.findByLicenseAndUser(license, user).get();

        licenseHistory.setStatus(status);
        licenseHistory.setChangeDate(new Date());

        licenseHistoryRepository.save(licenseHistory);
    }

    public void addDeviceLicenseHistory(License license, Device device){
        DeviceLicense newDeviceLicense = new DeviceLicense();
        newDeviceLicense.setLicense(license);
        newDeviceLicense.setDevice(device);
        newDeviceLicense.setActivationDate(new Date());

        deviceLicenseRepository.save(newDeviceLicense);
    }

    public License setLicenseTimestamps(License confirmedLicense){
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_MONTH, Math.toIntExact(confirmedLicense.getDuration()));
        confirmedLicense.setFirstActivationDate(new Date());
        confirmedLicense.setEndingDate(calendar.getTime());

        return confirmedLicense;
    }


}
