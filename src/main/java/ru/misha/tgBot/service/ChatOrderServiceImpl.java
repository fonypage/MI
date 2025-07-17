package ru.misha.tgBot.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.misha.tgBot.model.*;
import ru.misha.tgBot.repository.*;
import ru.misha.tgBot.service.dto.OrderSummary;

import java.util.List;

@Service
@Transactional
public class ChatOrderServiceImpl implements ChatOrderService {

    private final ClientRepository clientRepo;
    private final ClientOrderRepository orderRepo;
    private final CategoryRepository categoryRepo;
    private final ProductRepository productRepo;
    private final OrderProductRepository orderProductRepo;

    public ChatOrderServiceImpl(
            ClientRepository clientRepo,
            ClientOrderRepository orderRepo,
            CategoryRepository categoryRepo,
            ProductRepository productRepo,
            OrderProductRepository orderProductRepo) {
        this.clientRepo = clientRepo;
        this.orderRepo = orderRepo;
        this.categoryRepo = categoryRepo;
        this.productRepo = productRepo;
        this.orderProductRepo = orderProductRepo;
    }

    @Override
    public Client findOrCreateClient(long chatId, String userName) {
        return clientRepo.findByChatId(chatId)
                .orElseGet(() -> {
                    Client c = new Client();
                    c.setChatId(chatId);
                    c.setFullName(userName);
                    c.setAddress("");
                    c.setPhoneNumber("");
                    c.setExternalId(chatId);
                    return clientRepo.save(c);
                });
    }

    @Override
    public ClientOrder getOrCreateActiveOrder(Client client) {
        return orderRepo.findByClientAndStatus(client, 1)
                .orElseGet(() -> {
                    ClientOrder o = new ClientOrder();
                    o.setClient(client);
                    o.setStatus(1);
                    o.setTotal(0.0);
                    return orderRepo.save(o);
                });
    }

    @Override
    public List<Category> listCategories(Long parentId) {
        if (parentId == null) {
            return categoryRepo.findByParentIsNull();
        }
        return categoryRepo.findByParent_Id(parentId);
    }

    @Override
    public List<Product> listProducts(Long categoryId) {
        return productRepo.findByCategory_Id(categoryId);
    }

    @Override
    public Product addProductToOrder(ClientOrder order, Long productId) {
        Product product = productRepo.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found: " + productId));
        OrderProduct op = new OrderProduct();
        op.setClientOrder(order);
        op.setProduct(product);
        op.setCountProduct(1);
        orderProductRepo.save(op);
        return product;
    }

    @Override
    @Transactional
    public OrderSummary closeOrder(ClientOrder order) {
        List<OrderProduct> items = orderProductRepo.findByClientOrder(order);
        double total = items.stream()
                .mapToDouble(op -> op.getProduct().getPrice() * op.getCountProduct())
                .sum();
        order.setStatus(2);
        order.setTotal(total);
        orderRepo.save(order);
        return new OrderSummary(items, total);
    }


    @Override
    public OrderSummary getOrderSummary(ClientOrder order) {
        // Получаем все позиции (OrderProduct) для заказа
        List<OrderProduct> items = orderProductRepo.findByClientOrder(order);

        // Считаем сумму
        double total = 0.0;
        for (OrderProduct op : items) {
            total += op.getProduct().getPrice() * op.getCountProduct();
        }

        // Возвращаем сводку
        return new OrderSummary(items, total);
    }
}
