package com.chpark.study.reactive.domain;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.stereotype.Component;

// JdbcTemplate의 일부를 추출해서 JdbcOperations라는 인터페이스가 생성되었다.
// 인터페이스를 사용할 경우 계약과 세부 구현을 분리시킬 수 있는 장점이 있기 때문이다.
// 따라서 애플리케이션과 몽고DB를 분리하기위해서는 MongoOperations 인터페이스를 사용하는것이 좋다.

@Component
public class TemplateDatabaseLoader {

    @Bean
    CommandLineRunner initialize(MongoOperations mongoOperations) {
        System.out.println("TemplateDatabaseLoader.initialize");
        return (args) -> {
            mongoOperations.save(new Item("MackBookPro", 2_000_000));
            mongoOperations.save(new Item("4K Monitor", 500_000));
       };
    }
}
