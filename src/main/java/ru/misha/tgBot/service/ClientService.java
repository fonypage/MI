package ru.misha.tgBot.service;

import ru.misha.tgBot.model.Client;
import ru.misha.tgBot.model.ClientOrder;
import ru.misha.tgBot.model.Product;

import java.util.List;

/**
 * Сервис для работы с клиентами и их заказами:
 * <ul>
 *   <li>Получение списка заказов клиента</li>
 *   <li>Получение списка продуктов, которые закупал клиент</li>
 *   <li>Поиск клиентов по имени</li>
 * </ul>
 */
public interface ClientService {

    /**
     * Возвращает все заказы, сделанные клиентом с указанным идентификатором.
     *
     * @param clientId идентификатор клиента
     * @return список заказов; если клиент не найден или не сделал ни одного заказа — пустой список
     */
    List<ClientOrder> getClientOrders(Long clientId);

    /**
     * Возвращает уникальный список продуктов, которые покупал указанный клиент.
     *
     * @param clientId идентификатор клиента
     * @return список продуктов; может быть пустым, если клиент не делал покупок
     */
    List<Product> getClientProducts(Long clientId);

    /**
     * Ищет клиентов по части полного имени, игнорируя регистр.
     *
     * @param name подстрока полного имени клиента
     * @return список клиентов, чьи имена содержат указанную подстроку; если ничего не найдено — пустой список
     */
    List<Client> searchClientsByName(String name);
}