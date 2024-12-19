package ru.mtuci.license_service.models.rest.response;

import lombok.*;


@Data
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GenericResponse {
    private Integer statusCode;
    private String description;
}
