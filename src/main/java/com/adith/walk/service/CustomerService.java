package com.adith.walk.service;


import com.adith.walk.Entities.Address;
import com.adith.walk.Entities.Cart;
import com.adith.walk.Entities.Customer;
import com.adith.walk.dto.*;
import com.adith.walk.Entities.ConfirmToken;
import com.adith.walk.enums.UserRole;
import com.adith.walk.repositories.AddressRepository;
import com.adith.walk.repositories.CustomerRepository;
import com.adith.walk.repositories.TokenRepo;
import com.nimbusds.oauth2.sdk.util.singleuse.AlreadyUsedException;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.security.Principal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@AllArgsConstructor
public class CustomerService {
    CustomerRepository customerRepository;
    TwilioOtpService otpService;
    ModelMapper modelMapper;
    BCryptPasswordEncoder encoder;
    TokenService tokenService;

    AddressRepository addressRepository;


    public List<Customer> getAllCustomers() {

        return customerRepository.findAll();
    }

    public Customer getActiveCustomer(Principal principal){
        if (principal!=null&&principal.getName() != null) {
            return getCustomerByMobile(principal.getName());

        }
        Customer customer=new Customer();
        customer.setFirstName("SIGN IN");
        return customer;

    }

    public Customer saveCustomer(Customer customer){
        return customerRepository.save(customer);
    }

    public Customer getCustomerByMobile(String mobile) {

        return customerRepository.findCustomerByMobileNumber(mobile);

    }

    public List<Customer> getCustomerBetween(Integer start, Integer end){

        return customerRepository.findCustomerByUserIdBetween(start,end);
    }


//    public List<Customer>getPageOfCustomer(Integer page,Integer count){
//        Page<Customer> all = customerRepository.findAll(PageRequest.of(page, count));
//        List<Customer>CustomerPage=new ArrayList<>();
//        all.stream().forEach(customer -> CustomerPage.add(customer));
//       return CustomerPage;
//    }
    public CustomerPageDTO getPageOfCustomer(Integer pageNumber, Integer pageSize){

        Pageable pageable=PageRequest.of(pageNumber,pageSize);
        Page<Customer> all = customerRepository.findAll(pageable);

        CustomerPageDTO responseDTO=new CustomerPageDTO();
        responseDTO.setTotalPages(all.getTotalPages());
        responseDTO.setCustomers(all);
        responseDTO.setCurrentPageNumber(pageable.getPageNumber());

        return responseDTO;
    }




    public void deleteCustomerById(Integer id) {

        customerRepository.deleteById(id);
    }


    public Customer getCustomerById(Long userId) {


        return  customerRepository.findCustomerByUserId(userId);
    }

    public Customer registerCustomer(CustomerRegistrationRequest request) throws AlreadyUsedException {


       if( isMobileNumberExists(request.getMobileNumber())){
           throw new AlreadyUsedException("mobile number already taken");
       }
       if(isEmailIdExists(request.getEmail())){
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

        Cart cart=new Cart();
        cart.setTotalPrice(0l);
        cart.setTotalDiscount(0l);
        cart.setTotalMRP(0l);
        cart.setQuantity(0);
        customer.setCart(cart);

        customerRepository
                .save(customer);



        tokenService
                .saveConfirmationToken(confirmToken);

        return customer;
    }


    public boolean isMobileNumberExists(String mobile){

            return customerRepository.findCustomerByMobileNumber(mobile)!=null;
    }


    public boolean isEmailIdExists(String email){

        return customerRepository.findCustomerByEmail(email)!=null;
    }

    public boolean confirmToken(ConfirmTokenRequest request) {



       Customer existing=customerRepository.findCustomerByMobileNumber(request.getMobile());
       if(existing==null){
           throw new IllegalStateException("user not found");
       }
        ConfirmToken confirmationToken =existing.getConfirmToken();
        if(!request.getToken().equals(confirmationToken.getToken())){
            throw new IllegalStateException("invalid-otp");
        }

        if(customerRepository.findCustomerByMobileNumber(request.getMobile()).isEnabled()){
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

    public int enableAppUser(String mobile) {
        return customerRepository.enableCustomer(mobile);
    }

    public void sendNewToken(String mobile) {

        if(!isMobileNumberExists(mobile)){
            throw new UsernameNotFoundException("Mobile number is not registered");
        }


        Customer customer=customerRepository.findCustomerByMobileNumber(mobile);


        String token = otpService.sendOTP();
        ConfirmToken confirmToken =new ConfirmToken(token, LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(1));
        customer.setConfirmToken(confirmToken);

        customerRepository.save(customer);
    }

    public void resetPassword(PasswordResetRequest passwordResetRequest) {

        Customer customer=customerRepository.findCustomerByMobileNumber(passwordResetRequest.getMobile());
        customer.setPassword(encoder.encode(passwordResetRequest.getPassword()));
        customerRepository.save(customer);

    }

    public CustomerProfileUpdateRequest getProfileData(Principal principal) {
        CustomerProfileUpdateRequest request=new CustomerProfileUpdateRequest();
       modelMapper.map(customerRepository.findCustomerByMobileNumber(principal.getName()),request);

       return request;
    }


    public Customer updateProfileData(CustomerProfileUpdateRequest request,Principal principal) throws AlreadyUsedException {

        Customer customer=customerRepository.findCustomerByMobileNumber(request.getMobileNumber());
      if(isMobileNumberExists(request.getMobileNumber())&&!request.getMobileNumber().equals(principal.getName())){
          throw  new AlreadyUsedException("mobile number exists");
      }

        modelMapper.map(request,customer);

        return  customerRepository.save(customer);

    }

    public void updateCustomer(CustomerDTO customer) throws AlreadyUsedException {

        Customer existingCustomer= customerRepository.findCustomerByUserId(customer.getUserId());

        if(isMobileNumberExists(customer.getMobileNumber())&&!existingCustomer.getMobileNumber().equals(customer.getMobileNumber())){
            throw new AlreadyUsedException("mobile number already used");
        }

        if(isEmailIdExists(customer.getEmail())&&!customer.getEmail().equals(existingCustomer.getEmail())){
            throw new AlreadyUsedException("email  id already used");
        }


        modelMapper.map(customer,existingCustomer);

        customerRepository.save(existingCustomer);
    }



    public void registerCustomerAdmin(CustomerRegistrationRequestAdmin registrationRequest) throws AlreadyUsedException {

        registrationRequest.setPassword(encoder.encode(registrationRequest.getFirstName().substring(0,3)+"@"+registrationRequest.getMobileNumber().substring(0,3)));



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
}
