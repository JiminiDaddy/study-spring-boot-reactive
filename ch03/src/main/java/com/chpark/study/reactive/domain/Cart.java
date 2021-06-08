package com.chpark.study.reactive.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.annotation.Id;

import java.util.ArrayList;
import java.util.List;

// 장바구니: 장바구니 식별자, 장바구니에 담긴 상품 목록 필요
@EqualsAndHashCode
@Getter
public class Cart {
    @Id
    private String id;

    // 장바구니에 담긴 상품 목록
    private List<CartItem> cartItems;

    private Cart() {}

    public Cart(String id) {
        this(id, new ArrayList<>());
    }

    public Cart(String id, List<CartItem> cartItems) {
        this.id = id;
        this.cartItems = cartItems;
    }

}
