package com.adith.walk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class WalkApplication {

//	@Autowired
//	private TwilioConfig twilioConfig;
//
//	@PostConstruct
//	public void initTwilio(){
//		Twilio.init(twilioConfig.getAccountSid(),twilioConfig.getAuthToken());
//	}

	public static void main(String[] args) {
		SpringApplication.run(WalkApplication.class, args);
	}

}
