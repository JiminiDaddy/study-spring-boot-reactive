package com.chpark.study.reactive.web;

import com.chpark.study.reactive.domain.Dish;
import com.chpark.study.reactive.service.KitchenService;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class ServerController {
    private final KitchenService kitchenService;

    public ServerController(KitchenService kitchenService) {
        this.kitchenService = kitchenService;
    }

    // TEST_EVENT_STERAM_VALUE: text/event-stream
    // 요리를 만드는 API
    @GetMapping(value = "/server", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<Dish> serviceDished() {
       return kitchenService.getDishes();
    }

    // 요리를 서빙하는 API
    @GetMapping(value = "/served-dishes", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<Dish> deliverDishes() {
       return kitchenService.getDishes()
               .map(dish -> Dish.deliver(dish));
               //.map(Dish::deliver);
    }
}
