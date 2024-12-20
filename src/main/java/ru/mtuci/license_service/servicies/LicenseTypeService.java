package ru.mtuci.license_service.servicies;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mtuci.license_service.models.orm.LicenseType;
import ru.mtuci.license_service.models.rest.request.CreateLicenseType;
import ru.mtuci.license_service.models.rest.request.UpdateLicenseType;
import ru.mtuci.license_service.repositories.LicenseTypeRepository;
import ru.mtuci.license_service.utils.LicenseServiceException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LicenseTypeService {
    private final LicenseTypeRepository repository;

    public LicenseType createLicenseType(CreateLicenseType licenseType) throws Exception {
        String licenseTypeName = licenseType.getName();

        if (repository.findByName(licenseTypeName).isPresent()) {
            throw new LicenseServiceException("LicenseType already exists");
        }

        LicenseType newLicenseType = new LicenseType();
        newLicenseType.setName(licenseTypeName);
        newLicenseType.setDescription(licenseType.getDescription());
        newLicenseType.setDefaultDuration(licenseType.getDefaultDuration());

        LicenseType createdLicenseType = repository.save(newLicenseType);
        repository.flush();

        return createdLicenseType;
    }

    public LicenseType updateLicenseType(UpdateLicenseType licenseTypeData) throws Exception {
        Optional<LicenseType> licenseType = repository.findById(licenseTypeData.getId());

        if (licenseType.isEmpty()) {
            throw new LicenseServiceException("LicenseType not found");
        }

        LicenseType confurmedLicenseType = licenseType.get();
        confurmedLicenseType.setDefaultDuration(licenseTypeData.getDefaultDuration());
        confurmedLicenseType.setName(licenseTypeData.getName());
        confurmedLicenseType.setDescription(licenseTypeData.getDescription());

        LicenseType updatedLicenseType = repository.save(confurmedLicenseType);
        repository.flush();

        return updatedLicenseType;
    }

    public List<LicenseType> getLicenseTypes() {
        return repository.findAllLicenseTypes();
    }
}
