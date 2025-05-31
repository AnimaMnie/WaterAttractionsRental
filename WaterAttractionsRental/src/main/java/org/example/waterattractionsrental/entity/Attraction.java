package org.example.waterattractionsrental.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Attraction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Enumerated(EnumType.STRING)
    private AttractionType type;

    private boolean available;

    @OneToMany(mappedBy = "attraction", cascade = CascadeType.ALL)
    private List<Reservation> reservations;
}
