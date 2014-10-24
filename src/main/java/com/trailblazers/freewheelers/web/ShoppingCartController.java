package com.trailblazers.freewheelers.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(ShoppingCartController.URL)
public class ShoppingCartController {

    static final String URL = "/shoppingCart";
}
