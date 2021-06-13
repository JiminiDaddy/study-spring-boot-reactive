package com.chpark.study.reactive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.thymeleaf.TemplateEngine;
import reactor.blockhound.BlockHound;

@SpringBootApplication
public class CartApplication {
    public static void main(String[] args) {
        //BlockHound.install();     // 모든 Blocking API에 대해 호출 금지

        BlockHound.builder()
            .allowBlockingCallsInside(
                TemplateEngine.class.getCanonicalName(), "process"
            )
            .install();
        SpringApplication.run(CartApplication.class, args);
    }
}
