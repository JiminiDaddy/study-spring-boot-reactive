package com.chpark.study.reactive.domain;

public class Dish {
    private String menuName;

    private boolean delivered;

    public static Dish deliver(Dish dish) {
        // 함수형 프로그래밍에서는 객체를 변경하여 반환하지 않고 새로운 객체를 생성해서 반환하는것을 권장한다.
        Dish deliveredDish = new Dish(dish.menuName);
        deliveredDish.delivered = true;
        return deliveredDish;
    }

    public Dish(String menuName) {
        this.menuName = menuName;
    }

    public String getMenuName() {
        return menuName;
    }

    public boolean isDelivered() {
        return delivered;
    }

    @Override
    public String toString() {
        return "Dish{" + //
                "menuName='" + menuName + "\'" + //
                ", delivered=" + delivered + //
                "}";
    }
}
