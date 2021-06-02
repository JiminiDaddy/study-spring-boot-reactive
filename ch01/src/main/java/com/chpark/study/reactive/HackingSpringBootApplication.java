package com.chpark.study.reactive;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class HackingSpringBootApplication {
    public static void main(String[] args) {
        // 아래 한 줄 코드와 Controller, Service, Domain 클래스만 작성해도 웹 서비스가 만들어진다.
        // View-Resolver, Web-Method-Handler, Infrastructure 관련 Bean이 SpringBoot의 자동환경으로 구성하기 때문이다.
        // 만약 개발자가 해당 Bean을 주입하는 코드를 직접 구현하면 자동설정 대신 구현된 수동설정으로 Bean이 생성된다.
        SpringApplication.run(HackingSpringBootApplication.class, args);

        // Client API Test
        // curl -N -v localhost:8080/server
        // curl -N -v localhost:8080/served-dishes
    }
}
