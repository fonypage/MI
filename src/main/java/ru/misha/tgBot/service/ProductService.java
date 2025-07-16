package ru.misha.tgBot.service;

import ru.misha.tgBot.model.Product;

import java.util.List;

/**
 * Сервис для работы с продуктами:
 * <ul>
 *   <li>Поиск по категории</li>
 *   <li>Получение популярных товаров</li>
 *   <li>Поиск по имени</li>
 * </ul>
 */
public interface ProductService {

    /**
     * Возвращает список всех продуктов, принадлежащих указанной категории.
     *
     * @param categoryId идентификатор категории
     * @return список продуктов; если категория не найдена или в ней нет товаров — пустой список
     */
    List<Product> getProductsByCategoryId(Long categoryId);

    /**
     * Возвращает топ N самых популярных продуктов,
     * упорядоченных по убыванию общего количества проданных единиц.
     *
     * @param limit количество возвращаемых товаров; если {@code null} — используется значение по умолчанию
     * @return список самых популярных продуктов; может быть меньше {@code limit}, если товаров в базе меньше
     */
    List<Product> getTopPopularProducts(Integer limit);

    /**
     * Ищет продукты по подстроке в названии, игнорируя регистр.
     *
     * @param name подстрока названия продукта
     * @return список продуктов, содержащих указанную подстроку в названии; если ничего не найдено — пустой список
     */
    List<Product> searchProductsByName(String name);
}
