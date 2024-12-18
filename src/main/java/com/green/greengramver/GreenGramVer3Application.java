package com.green.greengramver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class GreenGramVer3Application {

    public static void main(String[] args) {
        SpringApplication.run(GreenGramVer3Application.class, args);
    }

}
