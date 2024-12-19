package ru.mtuci.license_service.models.orm;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "product", uniqueConstraints = @UniqueConstraint(columnNames = {"name"}))
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Product {

    @Id
    @NonNull
    @GeneratedValue
    private Long id;

    @NonNull
    private String name;

    private boolean isBlocked;
}

