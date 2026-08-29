package com.fooddelivery.order.model;

import java.util.List;

public class Order {
    private int userId;
    private int restaurantId;
    private List<String> items;

    public Order() {}

    public Order(int userId, int restaurantId, List<String> items) {
        this.userId = userId;
        this.restaurantId = restaurantId;
        this.items = items;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRestaurantId() {
        return restaurantId;
    }

    public void setRestaurantId(int restaurantId) {
        this.restaurantId = restaurantId;
    }

    public List<String> getItems() {
        return items;
    }

    public void setItems(List<String> items) {
        this.items = items;
    }
}
