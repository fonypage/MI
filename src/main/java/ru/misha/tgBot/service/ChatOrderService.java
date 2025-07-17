package ru.misha.tgBot.service;

import ru.misha.tgBot.model.Category;
import ru.misha.tgBot.model.Client;
import ru.misha.tgBot.model.ClientOrder;
import ru.misha.tgBot.model.Product;
import ru.misha.tgBot.service.dto.OrderSummary;

import java.util.List;

/**
 * Сервис обработки чата Telegram: создание/поиск клиента,
 * работа с заказом и добавление товаров.
 */
public interface ChatOrderService {

    Client findOrCreateClient(long chatId, String userName);

    ClientOrder getOrCreateActiveOrder(Client client);

    List<Category> listCategories(Long parentId);

    List<Product> listProducts(Long categoryId);

    Product  addProductToOrder(ClientOrder order, Long productId);

    OrderSummary closeOrder(ClientOrder order);

    OrderSummary getOrderSummary(ClientOrder order);

    ClientOrder findOrderById(Long orderId);

    void saveFeedback(String message, boolean positive);
}
