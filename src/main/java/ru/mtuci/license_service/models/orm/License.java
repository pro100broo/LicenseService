package ru.mtuci.license_service.models.orm;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "license")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class License {

    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private UserDetailsImpl user;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "type_id")
    private LicenseType type;

    private Date firstActivationDate;

    private Date endingDate;

    @NonNull
    private Long deviceCount;

    @NonNull
    private Long duration;

    @NonNull
    private String code;

    @Nullable
    private String description;

    private boolean blocked;
}

