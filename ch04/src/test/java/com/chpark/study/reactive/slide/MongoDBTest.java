package com.chpark.study.reactive.slide;

import com.chpark.study.reactive.domain.Item;
import com.chpark.study.reactive.domain.ItemRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import reactor.test.StepVerifier;

import static org.assertj.core.api.Assertions.assertThat;

// Data로 시작하는 Test Annotation은, 해당 DB관련 Bean만 생성하고, 그 외의 @Component로 선언된 bean은 무시한다.
@DataMongoTest
class MongoDBTest {
	@Autowired
	private ItemRepository itemRepository;

	@Test
	@DisplayName("Item이 ItemRepository에 잘 저장된다")
	void saveItem() {
		Item watch = new Item("watch", "pretty watch", 10000.0);

		itemRepository.save(watch)
			.as(StepVerifier::create)
			.expectNextMatches(item -> {
				assertThat(item.getId()).isNotNull();
				assertThat(item.getName()).isEqualTo("watch");
				assertThat(item.getPrice()).isEqualTo(10000.0);
				return true;
			})
			.verifyComplete()
		;
	}
}
