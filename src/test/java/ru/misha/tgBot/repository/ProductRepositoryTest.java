package ru.misha.tgBot.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.misha.tgBot.model.*;

import java.util.List;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepo;
    @Autowired
    private ClientRepository clientRepo;
    @Autowired
    private ClientOrderRepository orderRepo;
    @Autowired
    private OrderProductRepository opRepo;
    @Autowired
    private CategoryRepository catRepo;

    @Test
    void findDistinctProductsByClientId_shouldReturnUniqueProducts() {
        Category cat = new Category();
        cat.setName("C"); catRepo.save(cat);

        // создаём 2 продукта
        Product p1 = new Product(); p1.setName("P1"); p1.setCategory(cat); p1.setDescription("d1"); p1.setPrice(1.0); productRepo.save(p1);
        Product p2 = new Product(); p2.setName("P2"); p2.setCategory(cat); p2.setDescription("d2"); p2.setPrice(2.0); productRepo.save(p2);

        // клиент
        Client client = new Client();
        client.setExternalId(42L); client.setFullName("T"); client.setAddress("A"); client.setPhoneNumber("000"); client.setChatId(123456L); client.setExternalId(123456L); clientRepo.save(client);

        // заказ
        ClientOrder order = new ClientOrder();
        order.setClient(client); order.setStatus(1); order.setTotal(0.0); orderRepo.save(order);

        OrderProduct op1 = new OrderProduct(); op1.setClientOrder(order); op1.setProduct(p1); op1.setCountProduct(2); opRepo.save(op1);
        OrderProduct op2 = new OrderProduct(); op2.setClientOrder(order); op2.setProduct(p2); op2.setCountProduct(1); opRepo.save(op2);
        OrderProduct op3 = new OrderProduct(); op3.setClientOrder(order); op3.setProduct(p1); op3.setCountProduct(1); opRepo.save(op3);

        // вызов
        List<Product> result = productRepo.findDistinctProductsByClientId(client.getId());

        // проверка: в списке ровно два уникальных p1 и p2
        assertThat(result).hasSize(2)
                .extracting(Product::getName)
                .containsExactlyInAnyOrder("P1", "P2");
    }
}
