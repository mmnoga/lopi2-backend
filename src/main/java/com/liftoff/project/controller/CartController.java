package com.liftoff.project.controller;

import com.liftoff.project.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/carts")
@CrossOrigin("*")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

}
