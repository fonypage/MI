package ru.misha.tgBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.misha.tgBot.model.OrderProduct;
import ru.misha.tgBot.model.Product;

import java.util.List;

@RepositoryRestResource(path = "order-products", collectionResourceRel = "order-products")
public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
    List<OrderProduct> findByClientOrder_Client_Id(Long clientId);
    @Query("""
      SELECT DISTINCT op.product
        FROM OrderProduct op
       WHERE op.clientOrder.client.id = :clientId
    """)
    List<Product> findDistinctProductsByClientId(@Param("clientId") Long clientId);
}
