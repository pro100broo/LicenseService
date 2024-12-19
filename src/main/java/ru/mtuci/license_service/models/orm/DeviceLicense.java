package ru.mtuci.license_service.models.orm;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "device_license")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DeviceLicense {

    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "license_id")
    private License license;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "device_id")
    private Device device;

    @NonNull
    private Date activationDate;
}
