package com.adith.walk.service;

import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.text.DecimalFormat;
import java.util.Random;

@Service
public class TwilioOtpService {

    final Logger logger = LoggerFactory.getLogger(TwilioOtpService.class);


    public static final String ACCOUNT_SID = "AC1ae4e5f1a786d672af76fc0596bc2092";
    public static final String AUTH_TOKEN = "93c6718b0e7efc31d7347ec7072906a4";
    String OTP = null;


    public String sendOTP() {

        OTP = generateOTP();
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        Message message = Message.creator(
                new com.twilio.type.PhoneNumber("+919746959749"),
                new com.twilio.type.PhoneNumber("+16672708505"),
                "Your OTP for verification is" + OTP

        ).create();


//            System.out.println(message.getSid());
        logger.info(OTP);

        return OTP;
    }

    //6digit otp

    private String generateOTP() {

        return new DecimalFormat("000000").format(new Random().nextInt(999999));
    }
}
