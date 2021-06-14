package com.chpark.study.reactive;

import com.chpark.study.reactive.domain.*;
import com.chpark.study.reactive.service.CartService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)

class CartServiceTest {
	private CartService cartService;

	@MockBean
	private ItemRepository itemRepository;

	@MockBean
	private CartRepository cartRepository;

	// @MockBean이 생성한 코드 (MockBean은 아래와 같은 코드를 자동으로 생성시킨다.)
	/*
	@BeforeEach
	void setup() {
		itemRepository = Mockito.mock(ItemRepository.class);
		cartRepository = Mockito.mock(CartRepository.class);
	}
	*/

	@BeforeEach
	void setUp() {
		Item item = new Item("item01", "Notebook", "LG Gram", 2_000_000);
		CartItem cartItem = new CartItem(item);
		Cart cart = new Cart("MyCart", Collections.singletonList(cartItem));

		when(cartRepository.findById(anyString())).thenReturn(Mono.empty());
		when(itemRepository.findById(anyString())).thenReturn(Mono.just(item));
		when(cartRepository.save(any(Cart.class))).thenReturn(Mono.just(cart));

		cartService = new CartService(itemRepository, cartRepository);
	}

	@Test
	@DisplayName("빈 장바구니에 상품을 추가하면 구매상품목록에 추가된다1")
	void addItemToEmptyCartShouldProduceOneCartItem() {
		cartService.addItemToCart("MyCart", "item01")
			// subscribe, success, next 이벤트가 발생했을 때 로그가 출력되도록 하였다.
			.doOnSubscribe(subscription -> System.out.println("doOnsubscribe"))
			.doOnSuccess(cart -> System.out.println("doOnSuccess"))
			.doOnNext(cart -> System.out.println("doOnNext"))
			.doOnTerminate(() -> System.out.println("doOnTerminate"))
			// addItemToCart의 반환값 Mono<Cart>를 StepVerifier::create(리액터 테스크 모듈)로 연결하여
			// 테스트기능을 전담하는 리액터 타입 핸들러를 생성한다.
			.as(StepVerifier::create)
			// 결과식을 검증한다.
			.expectNextMatches(cart -> {
				// 장바구니에 담긴 구매상품의 갯수가 1개인지 확인한다.
				assertThat(cart.getCartItems()).extracting(CartItem::getQuantity)
					//.containsExactlyInAnyOrder(1);
					.containsExactly(1);
				// 구매상품과 item01 상품을 비교한다.
				assertThat(cart.getCartItems()).extracting(CartItem::getItem)
					.containsExactly(new Item("item01", "Notebook", "LG Gram", 2_000_000));
				return true;
			})
			// 리액티브 스트림의 complete 시그널이 발생하여, 리액터 플로우가 성공적으로 완료했음을 검증한다.
			.verifyComplete();
	}

	@Test
	@DisplayName("한 장바구니에 상품추가시 구매목록에 추가된다2")
	void alternativeWayToTest() {
		StepVerifier.create(
			cartService.addItemToCart("MyCart", "item01"))
			.expectNextMatches(cart -> {
				assertThat(cart.getCartItems()).extracting(CartItem::getQuantity).containsExactly(1);
				assertThat(cart.getCartItems()).extracting(CartItem::getItem).containsExactly(
					new Item("item01", "Notebook", "LG Gram", 2_000_000));
				return true;
			})
			.verifyComplete();
	}
}
