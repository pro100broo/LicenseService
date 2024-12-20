package ru.mtuci.license_service.models.rest.request;

import lombok.*;


@Data
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateProduct {
    private Long id;
    private String name;
    private String isBlocked;
}
