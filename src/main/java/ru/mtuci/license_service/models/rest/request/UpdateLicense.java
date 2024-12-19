package ru.mtuci.license_service.models.rest.request;

import lombok.*;

@Data
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateLicense {
    private String code;
    private Long productId;
    private Long licenseTypeId;
    private String description;
}
