package com.liftoff.project.model.order;

public enum DeliveryMethod___ {
    IN_POST(10.0),
    COURIER_SERVICE(20.0),
    IN_STORE_PICKUP(0.0);

    private final double cost;

    DeliveryMethod___(double cost) {
        this.cost = cost;
    }

    public double getCost() {
        return cost;
    }
}
