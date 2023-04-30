package com.rohan.discordclone.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/test")
public class TestController {
	
	@GetMapping
	public String testMethod() {
		return "Login Succeded and forwarded";
	}

}
