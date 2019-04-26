package com.anytec.apigateway;


import com.anytec.apigateway.config.RibbonConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.DiscoveryClient;
//import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


import java.util.List;

@SpringBootApplication
//@EnableDiscoveryClient
@RibbonClient(name = "USER",configuration = RibbonConfig.class)
@EnableCircuitBreaker
public class ApiGatewayApplication {

    public static void main(String[] args) {
        SpringApplication.run(ApiGatewayApplication.class, args);
    }

//    @Autowired
//    private DiscoveryClient discoveryClient;
//
//    @RequestMapping("/list")
//    @ResponseBody
//    public List<ServiceInstance> getInstances(){
//
//        List<ServiceInstance> user = discoveryClient.getInstances("user");
//        return user;
//    }


}
