package com.chpark.study.reactive.domain;

import org.springframework.data.repository.CrudRepository;

/* 애플리케이션 시작 시점에서는 Subscriber가 애플리케이션 시작 스레드로 하여금 이벤트 루프를 데드락 상태로 빠지게 만들 수 있다고 한다.
   따라서 시작 시점에서는 Reactive가 아닌 Blocking I/O를 사용해야 한다고 한다.
 */
public interface BlockingItemRepository extends CrudRepository<Item, String> {
}
