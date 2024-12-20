package ru.mtuci.license_service.models.rest.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Ticket {
    private Date currentDate;
    private Date lifetime;
    private Date activationDate;
    private Date expirationDate;
    private Long userId;
    private Long deviceId;
    private Long productId;
    private Long licenseTypeId;
    private boolean licenseBlocked;
    private String digitalSignature;
    private String info;
    private String status;
}
