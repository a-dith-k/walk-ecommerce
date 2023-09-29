package com.adith.walk.dto;

import com.adith.walk.entities.Customer;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

@Data
@NoArgsConstructor
public class CustomerPageDTO {

    Page<Customer> customers;

    Integer CurrentPageNumber;

    Integer totalPages;


}
