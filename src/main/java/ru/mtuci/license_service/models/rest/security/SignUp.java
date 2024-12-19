package ru.mtuci.license_service.models.rest.security;

import lombok.*;


@Data
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignUp {
    private String email;
    private String password;
    private String username;
}
