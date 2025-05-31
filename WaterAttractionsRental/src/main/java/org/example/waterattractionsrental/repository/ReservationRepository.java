package org.example.waterattractionsrental.repository;

import org.example.waterattractionsrental.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    boolean existsByAttractionIdAndStartTimeLessThanAndEndTimeGreaterThan(
            Long attractionId, LocalDateTime endTime, LocalDateTime startTime
    );
}
