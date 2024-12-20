package ru.mtuci.license_service.servicies;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.mtuci.license_service.models.orm.Device;
import ru.mtuci.license_service.models.orm.License;
import ru.mtuci.license_service.models.orm.UserDetailsImpl;
import ru.mtuci.license_service.models.rest.response.Ticket;
import ru.mtuci.license_service.utils.LicenseServiceException;

import java.security.*;
import java.util.*;

@Service
@RequiredArgsConstructor
public class TicketService {
    public Ticket generateTicket(License license, Device device, UserDetailsImpl user) throws LicenseServiceException {
        Ticket ticket = new Ticket();

        ticket.setCurrentDate(new Date());
        ticket.setUserId(user.getId());
        ticket.setDeviceId(device.getId());
        ticket.setProductId(license.getProduct().getId());
        ticket.setLicenseTypeId(license.getType().getId());

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.HOUR, 1);
        ticket.setLifetime(calendar.getTime());

        ticket.setActivationDate(license.getFirstActivationDate());
        ticket.setExpirationDate(license.getEndingDate());
        ticket.setLicenseBlocked(license.isBlocked());

        ticket.setInfo(license.getDescription());
        ticket.setDigitalSignature(makeSignature(ticket));
        ticket.setStatus("OK");
        return ticket;
    }

    private String makeSignature(Ticket ticket) throws LicenseServiceException {
        try{
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
            keyPairGenerator.initialize(2048);
            KeyPair keyPair = keyPairGenerator.generateKeyPair();
            PrivateKey privateKey = keyPair.getPrivate();
            ObjectMapper objectMapper = new ObjectMapper();
            String res = objectMapper.writeValueAsString(ticket);

            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateKey);
            signature.update(res.getBytes());

            return Base64.getEncoder().encodeToString(signature.sign());
        }
        catch (Exception exc){
            throw new LicenseServiceException(exc.getMessage());
        }
    }

}
