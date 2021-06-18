package com.chpark.study.reactive.service;

import com.chpark.study.reactive.domain.*;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class AltItemService {
	private final ItemRepository itemRepository;

	private final CartRepository cartRepository;

	public AltItemService(ItemRepository itemRepository, CartRepository cartRepository) {
		this.itemRepository = itemRepository;
		this.cartRepository = cartRepository;
	}

	public Mono<Cart> findCart(String cartId) {
		return cartRepository.findById(cartId);
	}

	public Flux<Item> findItems() {
		return itemRepository.findAll();
	}

	Mono<Item> saveItem(Item newItem) {
		return itemRepository.save(newItem);
	}

	Mono<Void> deleteItem(String itemId) {
		return itemRepository.deleteById(itemId);
	}

	public Mono<Cart> addItemToCart(String cardId, String itemId) {
		return findCart(cardId)
			.defaultIfEmpty(new Cart("My Cart"));
		/*
		Cart myCart = findCart(cardId)
			.defaultIfEmpty(new Cart("My Cart"))
			.block();

		return myCart.getCartItems().stream()
			.filter(cartItem -> cartItem.getItem().getId().equals(itemId))
			.findAny()
			.map(cartItem -> {
				cartItem.increment();
				return Mono.just(myCart);
			})
			.orElseGet(() ->
				itemRepository.findById(itemId)
					.map(item -> new CartItem(item))
					.map(cartItem -> {
						myCart.getCartItems().add(cartItem);
						return myCart;
					}))
			.flatMap(cart -> cartRepository.save(cart));
		*/
	}
}
