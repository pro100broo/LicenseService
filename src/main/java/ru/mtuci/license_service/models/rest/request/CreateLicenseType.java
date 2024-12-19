package ru.mtuci.license_service.models.rest.request;

import lombok.*;


@Data
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateLicenseType {
    private String name;
    private String description;
    private Long defaultDuration;
}
