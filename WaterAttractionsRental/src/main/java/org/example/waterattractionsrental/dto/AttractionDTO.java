package org.example.waterattractionsrental.dto;

import lombok.Builder;
import lombok.Data;
import org.example.waterattractionsrental.entity.AttractionType;

@Data
@Builder
public class AttractionDTO {
    private Long id;
    private String name;
    private AttractionType type;
    private Boolean available;
    private Double price;
}