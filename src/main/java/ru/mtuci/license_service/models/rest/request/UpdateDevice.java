package ru.mtuci.license_service.models.rest.request;

import lombok.*;


@Data
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateDevice {
    private Long deviceId;
    private String macAddress;
    private String name;
}
