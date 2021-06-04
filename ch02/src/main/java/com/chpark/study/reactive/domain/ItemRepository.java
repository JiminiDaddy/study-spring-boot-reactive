package com.chpark.study.reactive.domain;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;

public interface ItemRepository  extends ReactiveCrudRepository<Item, String> {
}
