package com.chpark.study.reactive.service;

import com.chpark.study.reactive.domain.Dish;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class KitchenService {

    private List<Dish> menu = new ArrayList<>();

    private Random picker = new Random();

    public KitchenService() {
        menu.add(new Dish("Pizza"));
        menu.add(new Dish("Pasta"));
        menu.add(new Dish("Chicken"));
    }

    // 요리 스트림 생성
    public Flux<Dish> getDishes() {
        // sink: 생성된 데이터가 빠져나가는 배출구
        // sink: 무작위로 제공되는 요리를 둘러싸는 Flux의 핸들, Flux에 포함될 원소를 동적으로 발행할 수 있도록 해준다.
        return Flux.<Dish>generate(sink -> sink.next(randomDish()))
                .delayElements(Duration.ofSeconds(1));
    }

    private Dish randomDish() {
        return menu.get(picker.nextInt(menu.size()));
    }
}
