package com.example.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = "spring.main.allow-bean-definition-overriding=true")
class DemoApplicationTests {

	@Test
	void contextLoads() {
	}

}
