package ru.mtuci.license_service.controllers.admin;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.mtuci.license_service.models.orm.LicenseType;
import ru.mtuci.license_service.models.rest.request.CreateLicenseType;
import ru.mtuci.license_service.models.rest.request.UpdateLicenseType;
import ru.mtuci.license_service.models.rest.response.GenericResponse;
import ru.mtuci.license_service.servicies.LicenseTypeService;


@RestController
@RequestMapping("/api/v1/adminService/licenseTypes")
@RequiredArgsConstructor
public class AdminLicenseTypeController {
    private final LicenseTypeService service;

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> createLicenseType(@RequestBody CreateLicenseType licenseType) {
        try {
            LicenseType createdLicenseType = service.createLicenseType(licenseType);
            return ResponseEntity.status(HttpStatus.OK).body(createdLicenseType);

        } catch (Exception ex) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GenericResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
        }
    }

    @PatchMapping("/update")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<?> updateLicenseType(@RequestBody UpdateLicenseType licenseType) {
        try {
            LicenseType updatedLicenseType = service.updateLicenseType(licenseType);
            return ResponseEntity.status(HttpStatus.OK).body(updatedLicenseType);

        } catch (Exception ex) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GenericResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
        }
    }
}
