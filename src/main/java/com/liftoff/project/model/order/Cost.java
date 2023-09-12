package com.liftoff.project.model.order;


public record Cost(
        Double productsTotal,
        Double delivery,
        Double total) {
}