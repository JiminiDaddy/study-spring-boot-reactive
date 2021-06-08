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
		return cartRepository.findById(cartId).log("found cart")
			.defaultIfEmpty(new Cart(cartId)).log("empty cart");
	}

	public Mono<Cart> addItemToCart(String cartId, String itemId) {
		return findCart(cartId)
			.flatMap(cart -> cart.getCartItems().stream()
				.filter(cartItem -> cartItem.getItem().getId().equals(itemId))
				.findAny()
				.map(cartItem -> {
					cartItem.increment();
					return Mono.just(cart).log("new CartItem");
				})
				.orElseGet(() -> itemRepository.findById(itemId)
					.log("fetched Item")
					.map(CartItem::new)
					.log("cart Item")
					.doOnNext(cartItem -> cart.getCartItems().add(cartItem))
					.map(cartItem -> cart)).log("added cartItem")
			)
			.log("cart with Another Item")
			.flatMap(cartRepository::save)
			.log("saved cart");
	}

	public Mono<Void> deleteItem(String itemId) {
		return itemRepository.deleteById(itemId);
	}
}
