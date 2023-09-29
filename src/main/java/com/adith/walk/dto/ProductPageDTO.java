package com.adith.walk.dto;

import com.adith.walk.entities.Product;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@NoArgsConstructor
public class ProductPageDTO {

    Page<Product> products;

    Integer CurrentPageNumber;

    Integer totalPages;


}
