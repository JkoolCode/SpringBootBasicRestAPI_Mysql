package com.practice.apirest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class PracticeBackend1Application implements CommandLineRunner{

	@Autowired
	private BCryptPasswordEncoder passwordEncoder;
	
	public static void main(String[] args) {
		SpringApplication.run(PracticeBackend1Application.class, args);
	}

	//Este metodo se ejecuta justo antes de inicializar el proyecto de SpringBoot
	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		String password = "123456";
		for(int i = 0; i < 4; i++) {
			String passwordBycrypt = passwordEncoder.encode(password);
			System.out.println(passwordBycrypt);
		}
	}

}
