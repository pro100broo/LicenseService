package ru.mtuci.license_service.models.rest.request;

import lombok.*;

@Data
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ActivateLicense {
    private String code;
    private Long deviceId;
}
