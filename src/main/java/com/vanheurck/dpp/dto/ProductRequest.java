package com.vanheurck.dpp.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class ProductRequest {

    @NotBlank
    @Size(max = 64)
    private String sku;

    @NotBlank
    @Size(max = 200)
    private String name;

    @Size(max = 100)
    private String category;

    @Size(max = 20)
    private String unitOfMeasure;

    private Boolean isActive = true;
}
