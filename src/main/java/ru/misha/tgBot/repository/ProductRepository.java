package ru.misha.tgBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.misha.tgBot.model.Product;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "products", collectionResourceRel = "products")
public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findByCategory_Id(Long categoryId);
    List<Product> findByNameContainingIgnoreCase(String name);
    @Query("""
      SELECT DISTINCT op.product
        FROM OrderProduct op
       WHERE op.clientOrder.client.id = :clientId
    """)
    List<Product> findDistinctProductsByClientId(@Param("clientId") Long clientId);
}
