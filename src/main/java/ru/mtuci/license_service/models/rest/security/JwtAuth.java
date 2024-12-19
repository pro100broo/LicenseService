package ru.mtuci.license_service.models.rest.security;

import lombok.*;


@Data
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JwtAuth {
    private String token;
}
