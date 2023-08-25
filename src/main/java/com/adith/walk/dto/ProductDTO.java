package com.adith.walk.dto;

import com.adith.walk.Entities.Category;
import com.adith.walk.Entities.Images;
import com.adith.walk.Entities.Stock;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
public class ProductDTO {


    private Integer productId;


    @NotBlank(message = "required")
    private String productName;

    @NotNull(message = "required")
    private Long productMrp;

    @NotNull
    private Long offerPrice;

    @NotBlank(message = "required")
    private String brand;

    @NotBlank(message = "required")
    private String productDescription;

    private Category category;

    private Stock stock;
}
