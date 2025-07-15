package ru.misha.tgBot.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.misha.tgBot.model.ClientOrder;
import ru.misha.tgBot.model.OrderProduct;
import ru.misha.tgBot.model.Product;
import ru.misha.tgBot.repository.ClientOrderRepository;
import ru.misha.tgBot.repository.ClientRepository;
import ru.misha.tgBot.repository.OrderProductRepository;
import ru.misha.tgBot.repository.ProductRepository;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
public class EntitiesServiceImpl implements EntitiesService {

    private final ProductRepository productRepo;
    private final ClientOrderRepository orderRepo;
    private final OrderProductRepository opRepo;
    private final ClientRepository clientRepo;

    public EntitiesServiceImpl(ProductRepository productRepo,
                               ClientOrderRepository orderRepo,
                               OrderProductRepository opRepo,
                               ClientRepository clientRepo) {
        this.productRepo = productRepo;
        this.orderRepo   = orderRepo;
        this.opRepo      = opRepo;
        this.clientRepo  = clientRepo;
    }

    @Override
    public List<Product> getProductsByCategoryId(Long categoryId) {
        return productRepo.findByCategory_Id(categoryId);
    }

    @Override
    public List<ClientOrder> getClientOrders(Long clientId) {
        return orderRepo.findByClient_Id(clientId);
    }

    @Override
    public List<Product> getClientProducts(Long clientId) {
        return opRepo.findByClientOrder_Client_Id(clientId)
                .stream()
                .map(OrderProduct::getProduct)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> getTopPopularProducts(Integer limit) {
        Map<Product, Long> countMap = opRepo.findAll().stream()
                .collect(Collectors.groupingBy(
                        OrderProduct::getProduct,
                        Collectors.summingLong(OrderProduct::getCountProduct)
                ));
        return countMap.entrySet().stream()
                .sorted(Map.Entry.<Product,Long>comparingByValue().reversed())
                .limit(limit)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}

