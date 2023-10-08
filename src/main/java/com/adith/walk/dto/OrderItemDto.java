package com.adith.walk.dto;

import com.adith.walk.entities.Product;
import com.adith.walk.entities.Size;
import lombok.Data;

@Data
public class OrderItemDto {


    private Long ItemId;

    private Product product;

    private Size productSize;

    private Long quantity;

    private Long totalMRP;

    private Long totalPrice;

}
