package com.chpark.study.reactive.service;

import com.chpark.study.reactive.domain.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CartService {
	private final ItemRepository itemRepository;
	private final CartRepository cartRepository;

	public CartService(ItemRepository itemRepository, CartRepository cartRepository) {
		this.itemRepository = itemRepository;
		this.cartRepository = cartRepository;
	}

	public Flux<Item> findAllItems() {
		return itemRepository.findAll();
	}

	public Mono<Cart> findCart(String cartId) {
		return cartRepository.findById(cartId).defaultIfEmpty(new Cart(cartId));
	}

	public Mono<Cart> addItemToCart(String cartId, String itemId) {
		// 1. cartId에 해당하는 장바구니를 찾는다. 만약 찾지 못했다면 cartId에 해당하는 장바구니를 하나 생성한다.
		return findCart(cartId)
			// 장바구니에 담겨있는 구매목록의 스트림을 가져온다.
			// flatMap에 의해 Stream<Cart>는 Stream<CartItem> 으로 스트림이 변경된다.
			.flatMap(cart -> cart.getCartItems().stream()
				// 구매목록에서 itemId와 일치하는 상품을 탐색한다.
				.filter(cartItem -> cartItem.getItem().getId().equals(itemId))
				// itemId와 일치하는 상품이 하나라도 발견되면
				.findAny()
				// 현재 스트림인 Stream<CartItem>이므로, 찾은 구매목록의 상품 개수를 1 증가시킨다.
				// 현재 장바구니를 반환한다.
				.map(cartItem -> {
					cartItem.increment();
					return Mono.just(cart);
				})
				// 만약 구매목록에서 itemId에 해당하는 상품을 찾지 못했다면
				.orElseGet(() -> {
					// 상품목록 저장소에서 itemId에 해당하는 상품을 가져오고,
					return itemRepository.findById(itemId)
						// 상품을 구매목록에 추가하고, Stream<Item>을 Stream<CartItem>으로 스트림을 변환한다.
						//.map(item -> new CartItem(item))
						.map(CartItem::new)
						// 구매목록이 생성되었으므로, 장바구니에 구매목록을 추가한다.
						.doOnNext(cartItem -> cart.getCartItems().add(cartItem))
						// Steram<CartItem>을 Stream<Cart>로 변환하여 장바구니를 반환한다.
						.map(cartItem -> cart);
				})
			)
			// 모든 작업이 끝났으면 장바구니를 업데이트 한다.
			// 장바구니는 1개 혹은 0개뿐이므로 Mono<Cart> 형태로 반환된다.
			.flatMap(cartRepository::save);
	}
}
