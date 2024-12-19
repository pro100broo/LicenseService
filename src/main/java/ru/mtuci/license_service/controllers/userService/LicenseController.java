package ru.mtuci.license_service.controllers.userService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mtuci.license_service.models.orm.License;
import ru.mtuci.license_service.models.orm.UserDetailsImpl;
import ru.mtuci.license_service.models.rest.request.ActivateLicense;
import ru.mtuci.license_service.models.rest.request.CreateLicense;
import ru.mtuci.license_service.models.rest.request.UpdateLicense;
import ru.mtuci.license_service.models.rest.response.GenericResponse;
import ru.mtuci.license_service.models.rest.response.Ticket;
import ru.mtuci.license_service.security.JwtService;
import ru.mtuci.license_service.servicies.LicenseService;
import ru.mtuci.license_service.servicies.UserService;
import ru.mtuci.license_service.utils.LicenseServiceException;

import java.util.List;


@RestController
@RequestMapping("/api/v1/userService/licenses")
@RequiredArgsConstructor
public class LicenseController {
    private final JwtService jwtService;
    private final UserService userService;
    private final LicenseService licenseService;

    @PostMapping("/create")
    public ResponseEntity<?> createLicense(@RequestBody CreateLicense licenseData, HttpServletRequest request) {
        try {
            License license = licenseService.createLicense(resolveUser(request), licenseData);
            return ResponseEntity.status(HttpStatus.OK).body(license);

        } catch (Exception ex) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GenericResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
        }
    }


    @PostMapping("/activate")
    public ResponseEntity<?> activateLicense(@RequestBody ActivateLicense licenseData, HttpServletRequest request) {
        try {
            Ticket ticket = licenseService.activateLicense(resolveUser(request), licenseData);
            return ResponseEntity.status(HttpStatus.OK).body(ticket);

        } catch (Exception ex) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GenericResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateLicense(@RequestBody UpdateLicense licenseData, HttpServletRequest request) {
        try {
            License license = licenseService.updateLicense(resolveUser(request), licenseData);
            return ResponseEntity.status(HttpStatus.OK).body(license);

        } catch (Exception ex) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GenericResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
        }
    }

    @PostMapping("/renew/{code}")
    public ResponseEntity<?> renewLicense(@PathVariable String code, HttpServletRequest request) {
        try {
            License license = licenseService.renewLicense(resolveUser(request), code);
            return ResponseEntity.status(HttpStatus.OK).body(license);

        } catch (Exception ex) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GenericResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
        }
    }

    @GetMapping("/info")
    public ResponseEntity<?> getLicenseInfo(@RequestBody ActivateLicense licenseData, HttpServletRequest request) {
        try {
            Ticket ticket = licenseService.getLicenseInfo(resolveUser(request), licenseData);
            return ResponseEntity.status(HttpStatus.OK).body(ticket);

        } catch (Exception ex) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GenericResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getLicenseInfo(HttpServletRequest request) {
        try {
            List<License> licenses = licenseService.getLicenses(resolveUser(request));

            if (licenses.isEmpty()){return ResponseEntity.noContent().build();}
            return ResponseEntity.status(HttpStatus.OK).body(licenses);


        } catch (Exception ex) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GenericResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
        }
    }


    public UserDetailsImpl resolveUser(HttpServletRequest request) throws LicenseServiceException {
        String username = jwtService.extractUserName(request);
        return userService.getByUsername(username);
    }
}
