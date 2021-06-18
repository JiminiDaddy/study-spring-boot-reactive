package com.chpark.study.reactive.blockhound;

import com.chpark.study.reactive.domain.*;
import com.chpark.study.reactive.service.AltItemService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.Duration;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
class BlockHoundIntegrationTest {

	private AltItemService itemService;

	@MockBean
	private ItemRepository itemRepository;

	@MockBean
	private CartRepository cartRepository;

	@BeforeEach
	void setUp() {
		Item sampleItem = new Item("item01", "TV", "SamsungTV", 3_000_000);
		CartItem sampleCartItem = new CartItem(sampleItem);
		Cart sampleCart = new Cart("My Cart", Collections.singletonList(sampleCartItem));

		// ono.empty()를 호출하면 MonoEmpty 클래스의 싱글턴 인스턴스가 반환되는데, 리액터는 이 인스턴스를 감자히고 런타임에 최적화한다...??
		when(cartRepository.findById(anyString())).thenReturn(Mono.<Cart>empty().hide());
		//when(cartRepository.findById(anyString())).thenReturn(Mono.empty());

		when(itemRepository.findById(anyString())).thenReturn(Mono.just(sampleItem));
		when(cartRepository.save(any(Cart.class))).thenReturn(Mono.just(sampleCart));

		itemService = new AltItemService(itemRepository, cartRepository);
	}

	@Test
	@DisplayName("Blockhound는 블록킹API를 검출한다")
	void blockhoundShouldTrapBlockingCall() {
		Mono.delay(Duration.ofSeconds(1))
			.flatMap(tick -> itemService.addItemToCart("My Cart", "item01"))
			.as(StepVerifier::create)
			.verifyErrorSatisfies(throwable -> {
				Assertions.assertThat(throwable).hasMessageContaining(
					"block()/blockFirst()/blockLast() are blocking");
			});
	}
}
