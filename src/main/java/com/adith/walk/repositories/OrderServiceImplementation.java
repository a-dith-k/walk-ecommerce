package com.adith.walk.repositories;

import com.adith.walk.Entities.Cart;
import com.adith.walk.Entities.CustomerOrder;
import com.adith.walk.Entities.CustomerOrderStatus;
import com.adith.walk.Entities.Payment;
import com.adith.walk.enums.OrderStatus;
import com.adith.walk.service.AddressService;
import com.adith.walk.service.CustomerService;
import com.adith.walk.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class OrderServiceImplementation implements OrderService{

    OrderRepo orderRepo;


    AddressService addressService;

    CustomerService customerService;


    @Override
    public boolean placeOrder(Cart cart, Payment payment, Principal principal) {
        CustomerOrder customerOrder=new CustomerOrder();

        customerOrder.setCustomer(customerService.getCustomerByMobile(principal.getName()));

        customerOrder.setCart(cart);
        customerOrder.setPayment(payment);
        List<CustomerOrderStatus>orderStatuses=new ArrayList<>();
        CustomerOrderStatus orderStatus=new CustomerOrderStatus();
        orderStatus.setIsCurrentStatus(true);
        orderStatus.setStatus(OrderStatus.PLACED);
        orderStatus.setTimeStampStatus(LocalDateTime.now());
        orderStatuses.add(orderStatus);
        customerOrder.setOrderStatus(orderStatuses);
        orderStatus.setOrder(customerOrder);
        customerOrder.setDeliveryAddress(addressService.getDefaultAddress(principal));
        customerOrder.setBillingAddress(addressService.getBillingAddress(principal));


        orderRepo.save(customerOrder);

        return true;

    }

    @Override
    public List<CustomerOrder> getAllOrderByPrincipal(Principal principal) {

       return orderRepo.findCustomerOrdersByCustomer(customerService.getCustomerByMobile(principal.getName()));

    }

    @Override
    public List<CustomerOrder> getAllOrders() {
        return orderRepo.findAll();
    }

    @Override
    public CustomerOrder getOrderById(Long orderId) {
        return orderRepo.findById(orderId).get();
    }

    @Override
    public void updateOrderStatus(Long orderId, OrderStatus orderStatus) {
        CustomerOrder order= orderRepo.findById(orderId).get();

        order.getOrderStatus().forEach(status->{
            if(status.getIsCurrentStatus().equals(true)){
                status.setIsCurrentStatus(false);
            }
        });
        order.getOrderStatus().forEach(status->{
            if(status.getStatus().equals(orderStatus)){
             System.out.println("here----------------------------------------------------------------");
                status.setTimeStampStatus(LocalDateTime.now());
                status.setIsCurrentStatus(true);
                orderRepo.save(order);
                return;
            }
        });

        System.out.println("here----------------------------------------------------------------");

        CustomerOrderStatus customerOrderStatus=new CustomerOrderStatus();
        customerOrderStatus.setStatus(orderStatus);
        customerOrderStatus.setTimeStampStatus(LocalDateTime.now());
        customerOrderStatus.setIsCurrentStatus(true);
        order.getOrderStatus().add(customerOrderStatus);

        orderRepo.save(order);
    }

    @Override
    public String getOrderStatus(Long orderId) {
        CustomerOrder customerOrder;
        if (orderRepo.findById(orderId).isPresent()){
            customerOrder= orderRepo.findById(orderId).get();
            List<CustomerOrderStatus> orderStatuses = customerOrder.getOrderStatus();
            CustomerOrderStatus customerOrderStatus = orderStatuses.stream().filter(CustomerOrderStatus::getIsCurrentStatus).findFirst().get();

            return customerOrderStatus.getStatus().name();
        }

        return "No Order Found";

    }
}
