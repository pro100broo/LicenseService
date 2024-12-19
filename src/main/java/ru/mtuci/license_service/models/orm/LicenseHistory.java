package ru.mtuci.license_service.models.orm;

import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "license_history")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LicenseHistory {

    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "license_id")
    private License license;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserDetailsImpl user;

    @NonNull
    private String status;

    @NonNull
    private Date changeDate;

    @Nullable
    private String description;
}

