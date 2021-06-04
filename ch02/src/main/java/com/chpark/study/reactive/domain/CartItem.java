package com.chpark.study.reactive.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;

// 구매 상품: 장바구니에 담긴 판매 상품의 구매 수량 필요
@EqualsAndHashCode
@Getter
public class CartItem {

    private Item item;

    private int quantity;

    private CartItem() {}

    public CartItem(Item item) {
        this.item = item;
        this.quantity = 1;
    }

    public void increment() {
        ++quantity;
    }
}
