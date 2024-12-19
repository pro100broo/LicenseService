package ru.mtuci.license_service.models.rest.request;

import lombok.*;


@Data
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateDevice {
    private String macAddress;
    private String name;
}
