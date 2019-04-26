package com.anytec.houseservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

@SpringBootApplication
@EnableDiscoveryClient
public class HouseServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(HouseServiceApplication.class, args);
    }

}
