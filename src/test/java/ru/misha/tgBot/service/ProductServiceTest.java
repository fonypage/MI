package ru.misha.tgBot.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import ru.misha.tgBot.model.Category;
import ru.misha.tgBot.model.Product;
import ru.misha.tgBot.repository.*;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@SpringBootTest
@Transactional
class ProductServiceTest {

    @Autowired
    private ProductService productService;
    @Autowired
    private CategoryRepository catRepo;
    @Autowired
    private ProductRepository prodRepo;

    @Test
    void whenGetProductsByCategory_thenReturnOnlyThose() {
        Category c = new Category();
        c.setName("X");
        catRepo.save(c);

        Product p1 = new Product();
        p1.setName("A");
        p1.setDescription("d");
        p1.setPrice(1.0);
        p1.setCategory(c);
        prodRepo.save(p1);

        Product p2 = new Product();
        p2.setName("B");
        p2.setDescription("d");
        p2.setPrice(2.0);
        p2.setCategory(c);
        prodRepo.save(p2);

        List<Product> out = productService.getProductsByCategoryId(c.getId());

        assertThat(out)
                .hasSize(2)
                .extracting(Product::getName)
                .containsExactlyInAnyOrder("A", "B");
    }
}
