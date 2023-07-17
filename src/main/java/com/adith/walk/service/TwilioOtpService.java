package com.adith.walk.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Random;

@Service
@EnableScheduling
public class TwilioOtpService {

    @Autowired
    CustomerService customerService;

    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;

    public static final String ACCOUNT_SID = "AC285623d3f62fcb00a32803fea8086450";
    public static final String AUTH_TOKEN = "75058d294298a1bea3d924f03f308eb2";
    String OTP=null;



        public String sendOTP(){

             OTP=generateOTP();

//            Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
//            Message message = Message.creator(
//                    new com.twilio.type.PhoneNumber("+917025845839"),
//                    new com.twilio.type.PhoneNumber("+12344373506"),
//                    OTP
//
//            ).create();
//
//
//            System.out.println(message.getSid());

            return  OTP;
        }
//    @Scheduled(initialDelay =1000*10000, fixedRate = 1000*60)
//    public void deleteOTP(){
//        customerService.deleteOTPByOTP(bCryptPasswordEncoder.encode(OTP));
//    }


    //6digit otp

    private String generateOTP(){

        return new DecimalFormat("000000").format(new Random().nextInt(999999));
    }
}
