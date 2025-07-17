package ru.misha.tgBot.OpenAI;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;
import ru.misha.tgBot.model.ClientOrder;
import ru.misha.tgBot.model.OrderProduct;
import ru.misha.tgBot.service.ChatOrderService;
import ru.misha.tgBot.service.dto.OrderSummary;

@Service
public class ToolService {

    private final ChatOrderService chatOrderService;

    public ToolService(ChatOrderService chatOrderService) {
        this.chatOrderService = chatOrderService;
    }

    @Tool(name = "getOrderDetails", description = "Получить детали заказа по ID")
    public String getOrderDetails(String orderIdStr) {
        Long orderId;
        try {
            orderId = Long.parseLong(orderIdStr);
        } catch (NumberFormatException e) {
            return "Неправильный формат ID заказа: " + orderIdStr;
        }

        ClientOrder order = chatOrderService.findOrderById(orderId);
        if (order == null) {
            return "Заказ №" + orderId + " не найден.";
        }

        OrderSummary summary = chatOrderService.getOrderSummary(order);
        if (summary.getItems().isEmpty()) {
            return "Заказ №" + orderId + " пуст.";
        }

        StringBuilder sb = new StringBuilder()
                .append("Детали заказа №").append(orderId).append(":\n");
        for (OrderProduct op : summary.getItems()) {
            String name = op.getProduct().getName();
            int qty = op.getCountProduct();
            double unitPrice = op.getProduct().getPrice();
            double lineTotal = unitPrice * qty;
            sb.append("- ")
                    .append(name)
                    .append(" x").append(qty)
                    .append(" = ").append(lineTotal).append("₽\n");
        }
        sb.append("Общая сумма: ").append(summary.getTotal()).append("₽");
        return sb.toString();
    }

    @Tool(name = "submitPositiveFeedback", description = "Отправить положительный отзыв")
    public String submitPositiveFeedback(String message) {
        // Сохраняем положительный отзыв через сервис
        chatOrderService.saveFeedback(message, true);
        return "Спасибо за ваш отзыв!";
    }

    @Tool(name = "submitNegativeFeedback", description = "Отправить отрицательный отзыв")
    public String submitNegativeFeedback(String message) {
        // Сохраняем отрицательный отзыв через сервис
        chatOrderService.saveFeedback(message, false);
        return "Ваше сообщение передано в службу поддержки.";
    }

    @Tool(name = "fallbackHelp", description = "Обработка непонятных запросов")
    public String fallbackHelp(String userMessage) {
        // Дополнительная логика при необходимости
        return "К сожалению, я вас не понял. Попробуйте сформулировать иначе или позвоните в поддержку.";
    }

}
