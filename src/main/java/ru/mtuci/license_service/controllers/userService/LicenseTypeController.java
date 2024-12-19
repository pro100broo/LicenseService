package ru.mtuci.license_service.controllers.userService;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mtuci.license_service.models.orm.LicenseType;
import ru.mtuci.license_service.models.rest.response.GenericResponse;
import ru.mtuci.license_service.servicies.LicenseTypeService;

import java.util.List;


@RestController
@RequestMapping("/api/v1/userService/licenseTypes")
@RequiredArgsConstructor
public class LicenseTypeController {
    private final LicenseTypeService service;

    @GetMapping("/getAll")
    public ResponseEntity<?> getLicenseTypes() {
        try {
            List<LicenseType> licenseTypes = service.getLicenseTypes();
            return ResponseEntity.status(HttpStatus.OK).body(licenseTypes);

        } catch (Exception ex) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GenericResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
        }
    }
}
