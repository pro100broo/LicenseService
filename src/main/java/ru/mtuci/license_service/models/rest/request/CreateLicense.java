package ru.mtuci.license_service.models.rest.request;

import lombok.*;

@Data
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateLicense {
    private Long productId;
    private Long licenseTypeId;
}
