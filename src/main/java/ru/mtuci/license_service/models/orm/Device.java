package ru.mtuci.license_service.models.orm;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "device", uniqueConstraints = @UniqueConstraint(columnNames = {"mac_address"}))
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Device {

    @Id
    @GeneratedValue
    private Long id;

    @NonNull
    private String name;

    @NonNull
    private String macAddress;

    @ManyToOne
    @JoinColumn(name = "userId")
    @JsonIgnore
    private UserDetailsImpl user;

}