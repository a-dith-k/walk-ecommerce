package com.adith.walk.dto;

import lombok.Data;

@Data
public class SalesReportDto {

    Long id;
    String name;
    Long size;
    Long quantity;
    Long totalPrice;
    Integer taxRate;
    Long finalPrice;
    
}
