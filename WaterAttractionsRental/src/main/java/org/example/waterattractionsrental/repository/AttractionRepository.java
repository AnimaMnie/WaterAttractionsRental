package org.example.waterattractionsrental.repository;

import org.example.waterattractionsrental.entity.Attraction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AttractionRepository extends JpaRepository<Attraction, Long> {
}
