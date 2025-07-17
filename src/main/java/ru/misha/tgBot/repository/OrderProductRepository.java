package ru.misha.tgBot.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.misha.tgBot.model.ClientOrder;
import ru.misha.tgBot.model.OrderProduct;
import ru.misha.tgBot.model.Product;

import java.util.List;

@RepositoryRestResource(path = "order-products", collectionResourceRel = "order-products")
public interface OrderProductRepository extends JpaRepository<OrderProduct, Long> {
    List<OrderProduct> findByClientOrder_Client_Id(Long clientId);
    @Query("""
      SELECT op.product
        FROM OrderProduct op
       GROUP BY op.product
       ORDER BY SUM(op.countProduct) DESC
    """)
    List<Product> findTopPopularProducts(Pageable pageable);
    List<OrderProduct> findByClientOrder_Id(Long orderId);
    List<OrderProduct> findByClientOrder(ClientOrder order);
}
