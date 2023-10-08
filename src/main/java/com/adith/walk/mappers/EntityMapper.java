package com.adith.walk.mappers;

import com.adith.walk.dto.SalesReportDto;
import com.adith.walk.entities.OrderItem;
import com.adith.walk.service.ProductService;
import org.springframework.stereotype.Component;

@Component
public class EntityMapper {

    final ProductService productService;

    public EntityMapper(ProductService productService) {
        this.productService = productService;
    }


    public SalesReportDto OrderItemToSalesReportDtoMapper(OrderItem orderItem) {

        SalesReportDto salesReport = new SalesReportDto();
        salesReport.setId(orderItem.getItemId());
        salesReport.setName(orderItem.getProduct().getProductName());
        salesReport.setSize((long) orderItem.getProductSize().getSizeNumber());
        salesReport.setQuantity(orderItem.getQuantity());
        salesReport.setTotalPrice(orderItem.getTotalPrice());
        salesReport.setTaxRate(orderItem.getProduct().getTaxRate());
        salesReport.setFinalPrice(orderItem.getTotalPrice() + productService.getProductTax(orderItem.getTotalPrice(), orderItem.getProduct().getTaxRate()));

        return salesReport;

    }

}
