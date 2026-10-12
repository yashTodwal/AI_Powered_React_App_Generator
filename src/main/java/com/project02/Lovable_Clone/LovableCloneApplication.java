package com.project02.Lovable_Clone;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.TimeZone;

@SpringBootApplication
public class LovableCloneApplication {
    static {
        java.util.TimeZone.setDefault(java.util.TimeZone.getTimeZone("UTC"));
    }
//    static {
//        System.setProperty("user.timezone", "UTC");
//        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
//    }
	public static void main(String[] args) {
        SpringApplication.run(LovableCloneApplication.class, args);
	}

}
