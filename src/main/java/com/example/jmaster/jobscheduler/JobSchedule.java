package com.example.jmaster.jobscheduler;

import java.util.Calendar;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.example.jmaster.entity.User;
import com.example.jmaster.repository.UserRepo;
import com.example.jmaster.service.EmailService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class JobSchedule {
	
	@Autowired 
	UserRepo userRepo;
	
	@Autowired
	EmailService emailService;
	
	
//	@Scheduled(fixedDelay = 60000)
//	public void hello() {
//		log.info("Hello!");
//		//emailService.testEmail();
//	}
	
	// giay - phut - gio - ngay - thang - thu
	@Scheduled(cron = "30 15 11 * * *")
	public void morning() {
		Calendar calendar = Calendar.getInstance();
		int date = calendar.get(Calendar.DATE);
		int month = calendar.get(Calendar.MONTH) + 1;
		//thang 1 tuong ung voi 0
		List<User> users = userRepo.searchByBirthday(date, month);
		for(User u:users) {
			log.info("Happy Birthday"+ u.getName());
			emailService.sendBirthdayEmail(u.getEmail(), u.getName());
		}
		
		log.info("Good Morning!");
	}
	
	
}
