package com.adith.walk.service.order.service;

import com.adith.walk.entities.*;
import com.adith.walk.enums.OrderStatus;
import com.adith.walk.repositories.OrderRepo;
import com.adith.walk.service.CustomerService;
import com.adith.walk.service.ProductService;
import com.adith.walk.service.SizeService;
import com.adith.walk.service.address.service.AddressServiceImpl;
import jakarta.servlet.http.HttpSession;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderServiceImplementation implements OrderService {

    Logger logger = LoggerFactory.getLogger(OrderServiceImplementation.class);

    final OrderRepo orderRepo;

    final AddressServiceImpl addressServiceImpl;

    final CustomerService customerService;

    final ProductService productService;

    final SizeService sizeService;

    public OrderServiceImplementation(OrderRepo orderRepo, AddressServiceImpl addressServiceImpl, CustomerService customerService, ProductService productService, SizeService sizeService) {
        this.orderRepo = orderRepo;
        this.addressServiceImpl = addressServiceImpl;
        this.customerService = customerService;
        this.productService = productService;
        this.sizeService = sizeService;
    }


    @Override
    public List<Orders> getAllOrderByPrincipal(Principal principal) {

        return orderRepo.findCustomerOrdersByCustomer(customerService.getCustomerByMobile(principal.getName()));

    }

    @Override
    public List<Orders> getAllOrders() {
        return orderRepo.findAll();
    }

    @Override
    public Orders getOrderById(Long orderId) {
        return orderRepo.findById(orderId).orElse(null);
    }

    @Override
    public void updateOrderStatus(Long orderId, OrderStatus orderStatus) {
        Orders orders = orderRepo.findById(orderId).orElse(null);

        if (orders != null) {
            orders.setOrderStatus(orderStatus);
            orderRepo.save(orders);
        }


    }

    @Override
    public String getOrderStatus(Long orderId) {
        Orders orders = orderRepo.findById(orderId).orElse(null);
        if (orders != null) {

            return orders.getOrderStatus().name();
        }

        return null;

    }

    @Override
    public Orders getCurrentOrder(Principal principal) {
        List<Orders> orders = getAllOrderByPrincipal(principal);


        return orders.stream().filter(Orders::isCurrentOrder).findFirst().orElse(null);


    }

    @Override
    public Orders createOrder(Cart cart, Principal principal, HttpSession session) {

        Orders orders = new Orders();

        List<CartItem> cartItems = cart.getItems();

        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem item : cart.getItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setProduct(item.getProduct());
            orderItem.setQuantity(item.getQuantity());
            orderItem.setTotalMRP(item.getTotalMRP());
            orderItem.setTotalPrice(item.getTotalPrice());
            orderItem.setOrder(orders);
            orderItem.setProductSize(item.getProductSize());
            orderItems.add(orderItem);
        }
        orders.setItems(orderItems);


        orders.setCurrentOrder(true);
        orders.setQuantity(cart.getQuantity());
        orders.setCustomer(cart.getCustomer());
        orders.setTotalDiscount(cart.getTotalDiscount());
        orders.setTotalMRP(cart.getTotalMRP());
        orders.setTotalPrice(cart.getTotalPrice());
        orders.setBillingAddress(addressServiceImpl.getBillingAddress(principal));
        orders.setDeliveryAddress(addressServiceImpl.getDefaultAddress(principal));
        orders.setIsPaymentDone(false);


        session.setAttribute("order", orders);

        return orders;

    }

    @Override
    public Orders buyNow(Integer productId, Principal principal, HttpSession session) {

        Orders order = new Orders();

        Product product = productService.getProductById(productId);

        List<OrderItem> orderItemList = new ArrayList<>();

        OrderItem orderItem = new OrderItem();
        orderItem.setProduct(product);
        orderItem.setOrder(order);
        orderItem.setQuantity(1L);
        orderItem.setTotalPrice(product.getOfferPrice());
        orderItem.setTotalMRP(product.getProductMrp());
        orderItemList.add(orderItem);

        order.setItems(orderItemList);


        order.setCurrentOrder(true);
        order.setQuantity(1);
        order.setTotalMRP(product.getProductMrp());
        order.setTotalPrice(product.getOfferPrice());
        order.setTotalDiscount(product.getProductMrp() - product.getOfferPrice());
        order.setCustomer(customerService.getCustomerByMobile(principal.getName()));
        order.setDeliveryAddress(addressServiceImpl.getDefaultAddress(principal));
        order.setBillingAddress(addressServiceImpl.getBillingAddress(principal));
        order.setIsPaymentDone(false);


        session.setAttribute("order", order);

        return order;
    }


    @Override
    public Orders placeOrder(Orders orders, Payment payment, Principal principal) {

        payment.setDateTime(LocalDateTime.now());
        payment.setTotalAmount(orders.getTotalPrice());
        orders.setPayment(payment);
        orders.setOrderStatus(OrderStatus.PLACED);
        OrderHistory orderHistory = new OrderHistory();
        orderHistory.setOrderTime(LocalDateTime.now());
        orders.setOrderHistory(orderHistory);
        orders.setIsPaymentDone(true);
        orders.getItems().forEach(orderItem -> {
            sizeService.removeStock(orderItem.getProductSize().getSizeId(), orderItem.getQuantity());


        });

        return orderRepo.save(orders);

    }


    @Override
    public Boolean isProductExistsInOrder(Principal principal, Integer productId) {


        if (principal == null) {
            return false;
        }
        List<Orders> orders = getAllOrderByPrincipal(principal);

        for (Orders order : orders) {
            for (OrderItem orderItem : order.getItems()) {
                return orderItem.getProduct().equals(productService.getProductById(productId));
            }

        }

        return false;
    }

    @Override
    public void cancelOrder(Long orderId) {

        Orders orders = orderRepo.findById(orderId).orElse(new Orders());

        orders.getItems().forEach(orderItem -> {
            sizeService.addStock(orderItem.getProductSize().getSizeId(), orderItem.getQuantity());
        });

        updateOrderStatus(orderId, OrderStatus.CANCELLED);
    }

    @Override
    public void returnOrder(Long orderId) {
        Orders orders = orderRepo.findById(orderId).orElse(new Orders());

        orders.getItems().forEach(orderItem -> {
            sizeService.addStock(orderItem.getProductSize().getSizeId(), orderItem.getQuantity());
        });


        updateOrderStatus(orderId, OrderStatus.RETURNED);


    }

    @Override
    public void save(Orders order) {
        orderRepo.save(order);
    }
}
