package ru.misha.tgBot.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.misha.tgBot.model.Product;
import ru.misha.tgBot.service.EntitiesService;

import java.util.List;

@RestController
@RequestMapping("/rest/products")
public class ProductRestController {

    private final EntitiesService service;

    public ProductRestController(EntitiesService service) {
        this.service = service;
    }

    @GetMapping("/search")
    public List<Product> byCategory(@RequestParam Long categoryId) {
        return service.getProductsByCategoryId(categoryId);
    }

    @GetMapping("/popular")
    public List<Product> popular(@RequestParam(defaultValue = "5") Integer limit) {
        return service.getTopPopularProducts(limit);
    }

    // доп. задание
    @GetMapping("/searchByName")
    public List<Product> byName(@RequestParam String name) {
        return service.searchProductsByName(name);
    }
}
