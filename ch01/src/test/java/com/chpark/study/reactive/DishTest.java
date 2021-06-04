package com.chpark.study.reactive;

import com.chpark.study.reactive.domain.Dish;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DishTest {
    @Test
	@DisplayName("서빙할 Dish는 요리된 Dish와 다른 인스턴스다")
    void staticInstance() {
        Dish dish = new Dish("Pizza");
        Assertions.assertThat(dish.isDelivered()).isFalse();

        Dish deliveredDish = Dish.deliver(dish);
        Assertions.assertThat(dish).isNotSameAs(deliveredDish);
        Assertions.assertThat(deliveredDish.isDelivered()).isTrue();
    }
}
