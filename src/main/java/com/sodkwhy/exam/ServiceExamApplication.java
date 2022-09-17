package com.sodkwhy.exam;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.ServletComponentScan;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@EnableSwagger2
@SpringBootApplication
@ServletComponentScan
public class ServiceExamApplication {

    public static void main(String[] args) {
        SpringApplication.run(ServiceExamApplication.class, args);
    }



}
