package com.adith.walk.service;


import com.adith.walk.dto.*;
import com.adith.walk.entities.Cart;
import com.adith.walk.entities.ConfirmToken;
import com.adith.walk.entities.Coupon;
import com.adith.walk.entities.Customer;
import com.adith.walk.enums.UserRole;
import com.adith.walk.repositories.AddressRepository;
import com.adith.walk.repositories.CustomerRepository;
import com.nimbusds.oauth2.sdk.util.singleuse.AlreadyUsedException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CustomerService {
    final CustomerRepository customerRepository;
    final TwilioOtpService otpService;
    final ModelMapper modelMapper;
    final BCryptPasswordEncoder encoder;
    final TokenService tokenService;

    final AddressRepository addressRepository;


    public CustomerService(CustomerRepository customerRepository,
                           TwilioOtpService otpService,
                           ModelMapper modelMapper,
                           BCryptPasswordEncoder encoder,
                           TokenService tokenService,
                           AddressRepository addressRepository) {
        this.customerRepository = customerRepository;
        this.otpService = otpService;
        this.modelMapper = modelMapper;
        this.encoder = encoder;
        this.tokenService = tokenService;
        this.addressRepository = addressRepository;


    }


    public List<Customer> getAllCustomers() {

        return customerRepository.findAll();
    }

    public Customer getActiveCustomer(Principal principal) {
        if (principal != null && principal.getName() != null) {
            return getCustomerByMobile(principal.getName());

        }
        Customer customer = new Customer();
        customer.setFirstName("SIGN IN");
        return customer;

    }

    public void saveCustomer(Customer customer) {
        customerRepository.save(customer);
    }

    public Customer getCustomerByMobile(String mobile) {

        return customerRepository.findCustomerByMobileNumber(mobile);

    }


    public CustomerPageDTO getPageOfCustomer(Integer pageNumber, Integer pageSize) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Customer> all = customerRepository.findAll(pageable);

        CustomerPageDTO responseDTO = new CustomerPageDTO();
        responseDTO.setTotalPages(all.getTotalPages());
        responseDTO.setCustomers(all);
        responseDTO.setCurrentPageNumber(pageable.getPageNumber());

        return responseDTO;
    }


    public void deleteCustomerById(Integer id) {

        customerRepository.deleteById(id);
    }


    public Customer getCustomerById(Long userId) {


        return customerRepository.findCustomerByUserId(userId);
    }

    public void registerCustomer(CustomerRegistrationRequest request) throws AlreadyUsedException {


        if (isMobileNumberExists(request.getMobileNumber())) {
            throw new AlreadyUsedException("mobile number already taken");
        }
        if (isEmailIdExists(request.getEmail())) {
            throw new AlreadyUsedException("email id already taken");
        }

        String token = otpService.sendOTP();


        Customer customer = modelMapper.map(request, Customer.class);
        customer
                .setUserRole(UserRole.ROLE_USER);
        customer
                .setPassword(encoder.encode(customer.getPassword()));

        ConfirmToken confirmToken =
                new ConfirmToken(token,
                        LocalDateTime.now(),
                        LocalDateTime.now().plusMinutes(1));

        customer
                .setConfirmToken(confirmToken);

        Cart cart = new Cart();
        cart.setTotalPrice(0L);
        cart.setTotalDiscount(0L);
        cart.setTotalMRP(0L);
        cart.setQuantity(0);
        customer.setCart(cart);

        customerRepository
                .save(customer);


        tokenService
                .saveConfirmationToken(confirmToken);


    }


    public boolean isMobileNumberExists(String mobile) {

        return customerRepository.findCustomerByMobileNumber(mobile) != null;
    }


    public boolean isEmailIdExists(String email) {

        return customerRepository.findCustomerByEmail(email) != null;
    }

    public boolean confirmToken(ConfirmTokenRequest request) {


        Customer existing = customerRepository.findCustomerByMobileNumber(request.getMobile());
        if (existing == null) {
            throw new IllegalStateException("user not found");
        }
        ConfirmToken confirmationToken = existing.getConfirmToken();
        if (!request.getToken().equals(confirmationToken.getToken())) {
            throw new IllegalStateException("invalid-otp");
        }

        if (customerRepository.findCustomerByMobileNumber(request.getMobile()).isEnabled()) {
            throw new IllegalStateException("already Verified");
        }

        if (confirmationToken.getConfirmedAt() != null) {
            throw new IllegalStateException("already confirmed");
        }

        LocalDateTime expiredAt = confirmationToken.getExpiresAt();

        if (expiredAt.isBefore(LocalDateTime.now())) {
            throw new IllegalStateException("otp expired");
        }
        tokenService.setConfirmedAt(request.getToken());
        enableAppUser(
                confirmationToken.getCustomer().getMobileNumber());
        return true;


    }

    public void enableAppUser(String mobile) {
        customerRepository.enableCustomer(mobile);
    }

    public void sendNewToken(String mobile) {

        if (!isMobileNumberExists(mobile)) {
            throw new UsernameNotFoundException("Mobile number is not registered");
        }


        Customer customer = customerRepository.findCustomerByMobileNumber(mobile);


        String token = otpService.sendOTP();
        ConfirmToken confirmToken = new ConfirmToken(token, LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(1));
        customer.setConfirmToken(confirmToken);

        customerRepository.save(customer);
    }

    public void resetPassword(PasswordResetRequest passwordResetRequest) {

        Customer customer = customerRepository.findCustomerByMobileNumber(passwordResetRequest.getMobile());
        customer.setPassword(encoder.encode(passwordResetRequest.getPassword()));
        customerRepository.save(customer);

    }

    public CustomerProfileUpdateRequest getProfileData(Principal principal) {
        CustomerProfileUpdateRequest request = new CustomerProfileUpdateRequest();
        modelMapper.map(customerRepository.findCustomerByMobileNumber(principal.getName()), request);

        return request;
    }


    public void updateProfileData(CustomerProfileUpdateRequest request, Principal principal) throws AlreadyUsedException {

        Customer customer = customerRepository.findCustomerByMobileNumber(request.getMobileNumber());
        if (isMobileNumberExists(request.getMobileNumber()) && !request.getMobileNumber().equals(principal.getName())) {
            throw new AlreadyUsedException("mobile number exists");
        }

        modelMapper.map(request, customer);

        customerRepository.save(customer);

    }

    public void updateCustomer(CustomerDTO customer) throws AlreadyUsedException {

        Customer existingCustomer = customerRepository.findCustomerByUserId(customer.getUserId());

        if (isMobileNumberExists(customer.getMobileNumber()) && !existingCustomer.getMobileNumber().equals(customer.getMobileNumber())) {
            throw new AlreadyUsedException("mobile number already used");
        }

        if (isEmailIdExists(customer.getEmail()) && !customer.getEmail().equals(existingCustomer.getEmail())) {
            throw new AlreadyUsedException("email  id already used");
        }


        modelMapper.map(customer, existingCustomer);

        customerRepository.save(existingCustomer);
    }


    public void registerCustomerAdmin(CustomerRegistrationRequestAdmin registrationRequest) throws AlreadyUsedException {

        registrationRequest.setPassword(encoder.encode(registrationRequest.getFirstName().substring(0, 3) + "@" + registrationRequest.getMobileNumber().substring(0, 3)));


        registerCustomer(modelMapper
                .map(registrationRequest,
                        CustomerRegistrationRequest.class));
    }

    public void enableUser(Long userId) {

        enableAppUser(customerRepository.findCustomerByUserId(userId).getMobileNumber());
    }

    public void disableUser(Integer userId) {

        customerRepository.disableCustomer(userId);
    }


    public List<Coupon> getCustomerCoupons(Principal principal) {

        return customerRepository.findCustomerByMobileNumber(principal.getName()).getCoupons();
    }
}
