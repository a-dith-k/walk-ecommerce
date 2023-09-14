package com.adith.walk.Entities;

import com.adith.walk.enums.OrderStatus;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Entity
@Data
public class CustomerOrderStatus {

    @SequenceGenerator(sequenceName = "statusIdGenerator",
    name = "statusIdGenerator",
    initialValue = 1,allocationSize = 1)


    @Id
    @GeneratedValue(strategy=GenerationType.SEQUENCE,generator = "statusIdGenerator")
    Long id;

    @ManyToOne()
    CustomerOrder order;

    OrderStatus status;

    LocalDateTime timeStampStatus;

    Boolean isCurrentStatus;
}
