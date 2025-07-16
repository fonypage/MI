package ru.misha.tgBot.service;

import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.misha.tgBot.model.Product;
import ru.misha.tgBot.repository.OrderProductRepository;
import ru.misha.tgBot.repository.ProductRepository;

import java.util.List;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepo;
    private final OrderProductRepository opRepo;

    public ProductServiceImpl(ProductRepository productRepo,
                              OrderProductRepository opRepo) {
        this.productRepo = productRepo;
        this.opRepo      = opRepo;
    }

    @Override
    public List<Product> getProductsByCategoryId(Long categoryId) {
        return productRepo.findByCategory_Id(categoryId);
    }

    @Override
    public List<Product> getTopPopularProducts(Integer limit) {
        int size = (limit != null ? limit : 5);
        return opRepo.findTopPopularProducts(PageRequest.of(0, size));
    }

    @Override
    public List<Product> searchProductsByName(String name) {
        return productRepo.findByNameContainingIgnoreCase(name);
    }
}