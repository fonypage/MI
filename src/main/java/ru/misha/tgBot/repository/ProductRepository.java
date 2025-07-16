package ru.misha.tgBot.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.misha.tgBot.model.Product;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "products", collectionResourceRel = "products")
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory_Id(Long categoryId);
    List<Product> findByNameContainingIgnoreCase(String name);
    @Query("""
      SELECT op.product
        FROM OrderProduct op
       GROUP BY op.product
       ORDER BY SUM(op.countProduct) DESC
    """)
    List<Product> findTopPopularProducts(Pageable pageable);
}
