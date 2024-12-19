package ru.mtuci.license_service.models.orm;


import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "license_type", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LicenseType {

    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    private String name;

    @NonNull
    private Long defaultDuration;

    @Nullable
    private String description;
}

