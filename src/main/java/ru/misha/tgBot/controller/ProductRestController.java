package ru.misha.tgBot.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.misha.tgBot.model.Product;
import ru.misha.tgBot.service.ProductService;

import java.util.List;

@RestController
@RequestMapping("/rest/products")
public class ProductRestController {

    private final ProductService productService;

    public ProductRestController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/search")
    public ResponseEntity<List<Product>> search(
            @RequestParam(required = false) Long categoryId,
            @RequestParam(required = false) String name) {
        if (name != null) {
            return ResponseEntity.ok(productService.searchProductsByName(name));
        }
        if (categoryId != null) {
            return ResponseEntity.ok(productService.getProductsByCategoryId(categoryId));
        }
        return ResponseEntity.badRequest().build();
    }

    @GetMapping("/popular")
    public ResponseEntity<List<Product>> popular(@RequestParam(defaultValue = "5") Integer limit) {
        return ResponseEntity.ok(productService.getTopPopularProducts(limit));
    }
}

