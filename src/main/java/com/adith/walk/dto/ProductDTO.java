package com.adith.walk.dto;

import com.adith.walk.entities.ProductCategory;
import com.adith.walk.entities.Stock;
import com.adith.walk.enums.CustomerCategory;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class ProductDTO implements Serializable {


    private Integer productId;


    @NotBlank(message = "required")
    private String productName;

    @NotNull(message = "required")
    private Long productMrp;

    private CustomerCategory customerCategory;

    @NotNull
    private Long offerPrice;

    @NotBlank(message = "required")
    private String brand;

    @NotBlank(message = "required")
    private String productDescription;

    private ProductCategory productCategory;
    private Long taxRate;
    private Stock stock;


}
