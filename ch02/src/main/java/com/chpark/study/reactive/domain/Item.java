package com.chpark.study.reactive.domain;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.annotation.Id;

// 판매 상품: 일련번호, 가격, 설명
@EqualsAndHashCode
@Getter
public class Item {
    // Spring-Data-Commons에 제공하는 @Id 사용하여 MongoDB의 ObjectId 값으로 사용한다.
    // ObjectId는 MongoDB 컬렉션의 _id 필드로 매핑된다.
    @Id
    private String id;

    private String name;

    private double price;

    private Item() {

    }

    Item(String name, double price) {
        this.name = name;
        this.price = price;
    }
}
