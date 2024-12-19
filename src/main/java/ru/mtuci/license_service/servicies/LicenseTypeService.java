package ru.mtuci.license_service.servicies;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mtuci.license_service.models.orm.LicenseType;
import ru.mtuci.license_service.models.rest.request.CreateLicenseType;
import ru.mtuci.license_service.repositories.LicenseTypeRepository;
import ru.mtuci.license_service.utils.LicenseServiceException;

import java.util.List;

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

    public List<LicenseType> getLicenseTypes() {
        return repository.findAllLicenseTypes();
    }
}
