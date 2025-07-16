package ru.misha.tgBot.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import ru.misha.tgBot.model.Product;
import ru.misha.tgBot.service.ProductService;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;



@WebMvcTest(ProductRestController.class)
class ProductRestControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private ProductService productService;

    @Test
    void searchByCategoryReturnsJson() throws Exception {
        when(productService.getProductsByCategoryId(1L))
                .thenReturn(List.of(new Product() {{
                    setId(1L);
                    setName("T");
                    setDescription("d");
                    setPrice(1.0);
                }}));

        mvc.perform(get("/rest/products/search")
                        .param("categoryId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("T"));
    }

    @Test
    void searchByNameReturnsJson() throws Exception {
        when(productService.searchProductsByName("foo"))
                .thenReturn(List.of(new Product() {{
                    setId(2L);
                    setName("Foo");
                    setDescription("desc");
                    setPrice(2.0);
                }}));

        mvc.perform(get("/rest/products/search")
                        .param("name", "foo"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Foo"));
    }

    @Test
    void popularReturnsJson() throws Exception {
        when(productService.getTopPopularProducts(3))
                .thenReturn(List.of(new Product() {{
                    setId(3L);
                    setName("Pop");
                    setDescription("popular");
                    setPrice(3.0);
                }}));

        mvc.perform(get("/rest/products/popular")
                        .param("limit", "3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Pop"));
    }
}


