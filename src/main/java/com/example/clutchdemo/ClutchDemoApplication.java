package com.example.clutchdemo;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.socket.config.annotation.EnableWebSocket;

@SpringBootApplication
@EnableWebSocket
public class ClutchDemoApplication {

    public static void main(String[] args)  {
        SpringApplication.run(ClutchDemoApplication.class, args);
    }

}
