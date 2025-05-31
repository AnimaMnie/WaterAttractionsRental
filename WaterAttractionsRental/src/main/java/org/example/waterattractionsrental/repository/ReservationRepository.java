package org.example.waterattractionsrental.repository;

import org.example.waterattractionsrental.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
}
