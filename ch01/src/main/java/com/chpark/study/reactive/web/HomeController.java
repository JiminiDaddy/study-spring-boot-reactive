package com.chpark.study.reactive.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Mono;

// Template을 사용한 웹 페이지 반환
@Controller
public class HomeController {
    @GetMapping("/home")
    // Mono<String>: 템플릿의 이름(문자열)을 리액티브 컨테이너인 Mono에 담아서 반환한다.
    // Mono: 0 또는 1개의 원소만 담을 수 있는 리액티브 발행자로써, 프로젝트 리액터에서 제공하는 구현체
    Mono<String> home() {
        // 리소스 경로의 templates 내부에 작성된 home이란 이름의 파일을 찾는다.
        // 예제는 의존성으로 타임리프를 주입하였으므로 타임리프 템플릿 엔진이 작동한다.
        return Mono.just("home");
    }
}
