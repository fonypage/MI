package ru.misha.tgBot.service;

import ru.misha.tgBot.model.Client;
import ru.misha.tgBot.model.ClientOrder;
import ru.misha.tgBot.model.Product;

import java.util.List;

public interface EntitiesService {
    List<Product> getProductsByCategoryId(Long id);
    List<ClientOrder> getClientOrders(Long id);
    List<Product> getClientProducts(Long id);
    List<Product> getTopPopularProducts(Integer limit);
    default List<Client> searchClientsByName(String name) {
        throw new UnsupportedOperationException("Доп. задание");
    }
    default List<Product> searchProductsByName(String name) {
        throw new UnsupportedOperationException("Доп. задание");
    }
}

