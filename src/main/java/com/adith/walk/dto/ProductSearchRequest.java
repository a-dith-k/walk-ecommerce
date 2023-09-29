package com.adith.walk.dto;

import com.adith.walk.entities.Images;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class ProductSearchRequest {

    private Integer productId;

    private String productName;

    private String brand;

    List<Images> list = new ArrayList<>();
}
