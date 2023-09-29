package com.adith.walk.controllers;

import com.adith.walk.entities.Orders;
import com.adith.walk.entities.Payment;
import com.adith.walk.entities.RazorPayDetails;
import com.adith.walk.enums.PaymentStatus;
import com.adith.walk.service.PaymentService;
import com.adith.walk.service.RazorPayService;
import com.adith.walk.service.cart.service.CartService;
import com.adith.walk.service.coupon.service.CouponService;
import com.adith.walk.service.order.service.OrderService;
import com.razorpay.Order;
import com.razorpay.RazorpayClient;
import com.razorpay.RazorpayException;
import jakarta.servlet.http.HttpSession;
import org.json.JSONObject;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@Controller
@RequestMapping("user/payment")
public class PaymentController {

    final OrderService orderService;

    final CartService cartService;
    final PaymentService paymentService;

    final Environment env;

    final RazorPayService razorPayService;

    final CouponService couponService;

    public PaymentController(OrderService orderService,
                             CartService cartService,
                             PaymentService paymentService,
                             Environment env,
                             RazorPayService razorPayService,
                             CouponService couponService) {

        this.orderService = orderService;
        this.cartService = cartService;
        this.paymentService = paymentService;
        this.env = env;
        this.razorPayService = razorPayService;
        this.couponService = couponService;
    }

    @PostMapping("")
    public String doPayment(@RequestParam String paymentMethod,
                            Principal principal,
                            @SessionAttribute("order") Orders orders) {

        if (paymentMethod.equals("cod")) {
            Payment payment = paymentService.doPayment(orders.getTotalPrice(), paymentMethod, PaymentStatus.PAID);
            orderService.placeOrder(orders, payment, principal);
            cartService.deleteCart(principal);
            return "/user/success";
        } else if (paymentMethod.equals("razorpay")) {
            Payment payment = paymentService.doPayment(orders.getTotalPrice(), paymentMethod, PaymentStatus.PENDING);

            Orders newOrder = orderService.placeOrder(orders, payment, principal);

            return "redirect:/user/payment/razorpay/" + newOrder.getId();

        }

        return "redirect:/user/checkout";
    }


    @GetMapping("/razorpay/{orderId}")
    public String razorpayPayment(Model model,
                                  @PathVariable Long orderId) {

        Orders order = orderService.getOrderById(orderId);

        if (order.getCoupon() != null) {
            model.addAttribute("totalAmount", order.getDiscountedPrice());
        } else {
            model.addAttribute("totalAmount", order.getTotalPrice());
        }

        model.addAttribute("orderId", order.getId());


        model.addAttribute("rzp_key_id", env.getProperty("rzp_key_id"));
        model.addAttribute("rzp_currency", env.getProperty("rzp_currency"));
        model.addAttribute("rzp_company_name", env.getProperty("rzp_company_name"));


        return "user/payment/razorPayment";
    }


    //endpoint for creating razorPay Payment ID
    @GetMapping("/razorpay/createOrderId/{amount}")
    @ResponseBody
    public String createPaymentOrderId(@PathVariable String amount) {
        String orderId = null;
        try {
            RazorpayClient razorpay = new RazorpayClient(env.getProperty("rzp_key_id"), env.getProperty("rzp_key_secret"));
            JSONObject orderRequest = new JSONObject();
            orderRequest.put("amount", amount); // amount in the smallest currency unit
            orderRequest.put("currency", env.getProperty("rzp_currency"));
            orderRequest.put("receipt", "order_rcptid_11");

            Order order = razorpay.orders.create(orderRequest);
            orderId = order.get("id");
        } catch (RazorpayException e) {
            // Handle Exception
            System.out.println(e.getMessage());
        }
        return orderId;

    }


    @PostMapping("/success/{amount}/{contactCount}/{companyName}/{currency}/{description}/{orderId}")
    public String paymentSuccess(Model model,
                                 Authentication authentication,
                                 @RequestParam("razorpay_payment_id") String razorpayPaymentId,
                                 @RequestParam("razorpay_order_id") String razorpayOrderId,
                                 @RequestParam("razorpay_signature") String razorpaySignature,
                                 @PathVariable Float amount,
                                 @PathVariable Integer contactCount,
                                 @PathVariable String companyName,
                                 @PathVariable String currency,
                                 @PathVariable String description,
                                 @PathVariable Long orderId,
                                 Principal principal,
                                 HttpSession session

    ) {
        Orders order = orderService.getOrderById(orderId);
        RazorPayDetails razorPayDetails = new RazorPayDetails(razorpayPaymentId, razorpayOrderId, razorpaySignature, amount, contactCount, companyName, currency, order);
        razorPayService.saveDetails(razorPayDetails);
        session.setAttribute("order", order);
        paymentService.updatePaymentStatus(order, PaymentStatus.PAID);
        couponService.provideCoupon(principal, order.getTotalPrice());
        cartService.deleteCart(principal);

        return "redirect:/user/order/success";
    }

}
