package com.adith.walk.repositories;

import com.adith.walk.Entities.*;
import com.adith.walk.service.AddressService;
import com.adith.walk.service.CustomerService;
import com.adith.walk.service.OrderService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.util.List;
import java.util.Optional;

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
        customerOrder.setStatus("order placed");
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
    public void updateOrderStatus(Long orderId, String orderStatus) {
        CustomerOrder order= orderRepo.findById(orderId).get();

        order.setStatus(orderStatus);

        orderRepo.save(order);
    }
}
