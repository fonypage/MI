package ru.misha.tgBot.service.dto;

import ru.misha.tgBot.model.OrderProduct;

import java.util.List;

/**
 * Итоговое описание заказа: список позиций и общая сумма.
 */
public class OrderSummary {
    /** Список позиций заказа */
    private final List<OrderProduct> items;
    /** Общая сумма заказа */
    private final double total;

    public OrderSummary(List<OrderProduct> items, double total) {
        this.items = items;
        this.total = total;
    }

    public List<OrderProduct> getItems() {
        return items;
    }

    public double getTotal() {
        return total;
    }

    /**
     * Формирует текст для отправки в Telegram:
     * \"Название продукта количество = стоимость₽\" и строку \"Итого: сумма₽\".
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Ваш заказ:\n");
        for (OrderProduct op : items) {
            sb.append(op.getProduct().getName())
                    .append(" x").append(op.getCountProduct())
                    .append(" = ")
                    .append(op.getProduct().getPrice() * op.getCountProduct())
                    .append("₽\n");
        }
        sb.append("Итого: ").append(total).append("₽");
        return sb.toString();
    }
}
