package ru.mtuci.license_service.controllers.userService;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.mtuci.license_service.models.orm.Device;
import ru.mtuci.license_service.models.orm.UserDetailsImpl;
import ru.mtuci.license_service.models.rest.request.CreateDevice;
import ru.mtuci.license_service.models.rest.request.UpdateDevice;
import ru.mtuci.license_service.models.rest.response.GenericResponse;
import ru.mtuci.license_service.security.JwtService;
import ru.mtuci.license_service.servicies.DeviceService;
import ru.mtuci.license_service.servicies.UserService;
import ru.mtuci.license_service.utils.LicenseServiceException;

import java.util.List;


@RestController
@RequestMapping("/api/v1/userService/devices")
@RequiredArgsConstructor
public class DeviceController {
    private final JwtService jwtService;
    private final UserService userService;
    private final DeviceService deviceService;

    @PostMapping("/create")
    public ResponseEntity<?> createDevice(@RequestBody CreateDevice deviceData, HttpServletRequest request) {
        try {
            Device device = deviceService.createDevice(resolveUser(request), deviceData);
            return ResponseEntity.status(HttpStatus.OK).body(device);

        } catch (Exception ex) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GenericResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
        }
    }

    @GetMapping("/getAll")
    public ResponseEntity<?> getDevices(HttpServletRequest request) {
        try {
            List<Device> devices = deviceService.getDevices(resolveUser(request));

            if (devices.isEmpty()){return ResponseEntity.noContent().build();}
            return ResponseEntity.status(HttpStatus.OK).body(devices);

        } catch (Exception ex) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GenericResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
        }
    }

    @PostMapping("/update")
    public ResponseEntity<?> updateDevice(@RequestBody UpdateDevice deviceData, HttpServletRequest request) {
        try {
            Device device = deviceService.updateDevice(resolveUser(request), deviceData);
            return ResponseEntity.status(HttpStatus.OK).body(device);

        } catch (Exception ex) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new GenericResponse(HttpStatus.INTERNAL_SERVER_ERROR.value(), ex.getMessage()));
        }
    }

    @GetMapping("/delete/{device_id}")
    public ResponseEntity<?> deleteDevice(@PathVariable Long device_id, HttpServletRequest request) {
        try {
            String username = jwtService.extractUserName(request);
            UserDetailsImpl user = userService.getByUsername(username);
            deviceService.deleteDevice(device_id, user);

            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(new GenericResponse(
                            200,
                            String.format("Device %d successfully deleted", device_id))
                    );

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
