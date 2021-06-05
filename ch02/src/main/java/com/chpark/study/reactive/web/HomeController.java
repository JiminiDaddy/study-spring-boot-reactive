package com.chpark.study.reactive.web;

import com.chpark.study.reactive.domain.Cart;
import com.chpark.study.reactive.domain.Item;
import com.chpark.study.reactive.service.CartService;
import com.chpark.study.reactive.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.result.view.Rendering;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import static com.chpark.study.reactive.common.CartConstants.DEFAULT_CART_ID;
import static com.chpark.study.reactive.common.CartConstants.VIEW_NAME_HOME;
import static com.chpark.study.reactive.common.CartConstants.VIEW_NAME_SEARCH;

@Controller
public class HomeController {
	private final CartService cartService;

	private final ItemService itemService;

    public HomeController(CartService cartService, ItemService itemService) {
        this.cartService = cartService;
        this.itemService = itemService;
    }

    // 메인 화면 (장바구니 조회)
    @GetMapping
    Mono<Rendering> home() {
        Flux<Item> allItems = cartService.findAllItems();
        Mono<Cart> cart = cartService.findCart(DEFAULT_CART_ID);
        return Mono.just(Rendering.view(VIEW_NAME_HOME)
            .modelAttribute("items", allItems)
            .modelAttribute("cart", cart)
            .build()
        );
    }

    // 장바구니에 상품 추가
    @PostMapping("/add/{id}")
    Mono<String> addToCart(@PathVariable(name = "id") String itemId) {
        Mono<Cart> cart = cartService.addItemToCart(DEFAULT_CART_ID, itemId);
        return cart.thenReturn("redirect:/");
    }

    // 상품 제거
    @DeleteMapping("/delete/{id}")
    Mono<String> deleteItem(@PathVariable(name = "id") String itemId) {
        return cartService.deleteItem(itemId).thenReturn("redirect:/");
    }

    // 상품 검색
    @GetMapping("/search")
    Mono<Rendering> search(@RequestParam(required = false) String name,
                      @RequestParam(required = false) String description,
                      @RequestParam boolean useAnd) {
    	return Mono.just(Rendering.view(VIEW_NAME_SEARCH)
            .modelAttribute("results", itemService.searchByExample(name, description, useAnd))
            .build());
    }
}
