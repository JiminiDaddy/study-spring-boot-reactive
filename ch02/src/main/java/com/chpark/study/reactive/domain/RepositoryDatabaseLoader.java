package com.chpark.study.reactive.domain;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

// Reactive에서 Blocking Repository를 사용해서 Blocking I/O작업을 한다면 Reactive의 장점이 사라진다.
// Reactive에서 이렇게 Blocking Repository를 구현해서 사용하는것을 다른 팀원이 참조할 경우, Blocking Repository를 추가로 구현할 수도 있다.
// (그래도 되는줄 알고)
// 따라서 위와같은 문제를 막기위한 가장 좋은 방법은 Blocking Repository을 사용하지 않는 것이다.
// 따라서 이 클래스는 사용하지 않는다. (예제로만, 이렇게 구현할 수 있다는 정도로만 사용함)

//@Component
public class RepositoryDatabaseLoader {

    // CommandLineRunner: 스프링 부트가, 애플리케이션 실행 후 자동으로 실행해주는 특수한 컴포넌트
    // 내부에 run 메서드 하나만 가지고 있는 함수형 인터페이스다.
    // CommandLineRunner 객체가 실행될 때 (run메서드) 실행 순서는 보장되지 않는다. (순서에 의존한 작업은 하면안됨)
    @Bean
    CommandLineRunner initialize(BlockingItemRepository repository) {
        System.out.println("RepositoryDatabaseLoader.initialize");
        // BlockingRepository를 사용하였으므로 애플리케이션 시작 시점에서 네티와 충돌할 위험은 없다.
        // 람다식을 사용해서 굳이 CommandLineRunner를 상속받고 run 메서드를 오버라이딩 하지 않아도 된다.
        return (args) -> {
            repository.save(new Item("MackBookPro", 2_000_000));
            repository.save(new Item("4K Monitor", 500_000));
        };
    }
}
