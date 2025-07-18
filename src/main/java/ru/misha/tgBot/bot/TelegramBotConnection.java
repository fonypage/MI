package ru.misha.tgBot.bot;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.misha.tgBot.OpenAI.OpenAiService;
import ru.misha.tgBot.OpenAI.ToolService;
import ru.misha.tgBot.service.ChatOrderService;
import ru.misha.tgBot.model.Client;
import ru.misha.tgBot.model.ClientOrder;
import ru.misha.tgBot.model.Category;
import ru.misha.tgBot.model.Product;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.request.AnswerCallbackQuery;
import com.pengrad.telegrambot.model.request.InlineKeyboardMarkup;
import com.pengrad.telegrambot.model.request.InlineKeyboardButton;
import com.pengrad.telegrambot.model.request.Keyboard;
import com.pengrad.telegrambot.model.request.ReplyKeyboardMarkup;
import com.pengrad.telegrambot.model.request.KeyboardButton;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.Message;
import ru.misha.tgBot.service.dto.OrderSummary;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@Component
public class TelegramBotConnection {

    private final TelegramBot bot;
    private final ChatOrderService chatOrderService;
    private final OpenAiService openAiService;
    private final ToolService toolService;

    // История диалога для OpenAI
    private final Map<Long, List<Message>> aiHistories = new ConcurrentHashMap<>();

    public TelegramBotConnection(
            @Value("${telegram.bot.token}") String token,
            ChatOrderService chatOrderService,
            OpenAiService openAiService,
            ToolService toolService) {
        this.bot = new TelegramBot(token);
        this.chatOrderService = chatOrderService;
        this.openAiService = openAiService;
        this.toolService = toolService;
    }

    @PostConstruct
    public void init() {
        bot.setUpdatesListener(updates -> {
            updates.forEach(this::processUpdate);
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });
    }

    private void processUpdate(Update update) {
        if (update.callbackQuery() != null) {
            handleCallback(update.callbackQuery());
        } else if (update.message() != null && update.message().text() != null) {
            handleMessage(update.message());
        }
    }

    private void handleMessage(com.pengrad.telegrambot.model.Message message) {
        long chatId = message.chat().id();
        String text = message.text().trim();
        String userName = message.from().firstName();
        Client client = chatOrderService.findOrCreateClient(chatId, userName);
        ClientOrder order = chatOrderService.getOrCreateActiveOrder(client);

        // Основные команды бота
        if ("/start".equalsIgnoreCase(text) || "В основное меню".equalsIgnoreCase(text)) {
            sendMainMenu(chatId);
            return;
        }

        if ("Оформить заказ".equalsIgnoreCase(text)) {
            OrderSummary summary = chatOrderService.getOrderSummary(order);
            if (summary.getItems().isEmpty()) {
                bot.execute(new SendMessage(chatId, "Ваш заказ пуст!"));
                return;
            }
            InlineKeyboardMarkup inline = new InlineKeyboardMarkup(
                    new InlineKeyboardButton[]{ new InlineKeyboardButton("Заказать").callbackData("confirm_order") },
                    new InlineKeyboardButton[]{ new InlineKeyboardButton("Главное меню").callbackData("main_menu") }
            );
            bot.execute(new SendMessage(chatId, summary.toString()).replyMarkup(inline));
            return;
        }

        Optional<Category> selectedCat = chatOrderService.listCategories(null).stream()
                .filter(c -> c.getName().equalsIgnoreCase(text)).findFirst();
        if (selectedCat.isPresent()) {
            Category cat = selectedCat.get();
            List<Category> subs = chatOrderService.listCategories(cat.getId());
            if (!subs.isEmpty()) {
                sendSubCategoryMenu(chatId, cat.getId());
            } else {
                sendProductList(chatId, cat.getId(), cat.getName());
            }
            return;
        }

        List<Category> allSubs = chatOrderService.listCategories(null).stream()
                .flatMap(c -> chatOrderService.listCategories(c.getId()).stream())
                .collect(Collectors.toList());
        Optional<Category> subOpt = allSubs.stream()
                .filter(c -> c.getName().equalsIgnoreCase(text)).findFirst();
        if (subOpt.isPresent()) {
            sendProductList(chatId, subOpt.get().getId(), subOpt.get().getName());
            return;
        }

        // AI fallback через OpenAI для всех остальных сообщений
        List<Message> history = aiHistories.computeIfAbsent(chatId, id -> new LinkedList<>());
        history.add(new UserMessage(text));
        String aiReply = openAiService.sendMessage(history, text);
        history.add(new AssistantMessage(aiReply));
        bot.execute(new SendMessage(chatId, aiReply));
    }

    private void handleCallback(CallbackQuery cb) {
        long chatId = cb.message().chat().id();
        String data = cb.data();
        String userName = cb.from().firstName();
        Client client = chatOrderService.findOrCreateClient(chatId, userName);
        ClientOrder order = chatOrderService.getOrCreateActiveOrder(client);

        if (data.startsWith("product:")) {
            Long productId = Long.valueOf(data.split(":")[1]);
            Product p = chatOrderService.addProductToOrder(order, productId);
            bot.execute(new AnswerCallbackQuery(cb.id()).text("'" + p.getName() + "' добавлен в заказ."));
        } else if ("main_menu".equals(data)) {
            sendMainMenu(chatId);
        } else if ("confirm_order".equals(data)) {
            OrderSummary summary = chatOrderService.closeOrder(order);
            String address = client.getAddress() != null ? client.getAddress() : "уточните адрес";
            long orderId = order.getId();
            String confirmText = String.format(
                    "Заказ №%d подтвержден. Курьер уже едет по адресу. Приблизительное время доставки 90 мин. Вы можете оставить отзыв о доставке прямо в чате.",
                    orderId, address
            );
            bot.execute(new SendMessage(chatId, confirmText));
            chatOrderService.getOrCreateActiveOrder(client);
            sendMainMenu(chatId);
        }
    }

    private void sendMainMenu(long chatId) {
        Keyboard menu = new ReplyKeyboardMarkup(
                new KeyboardButton[]{ new KeyboardButton("Пицца"), new KeyboardButton("Роллы"),
                        new KeyboardButton("Бургеры"), new KeyboardButton("Напитки") },
                new KeyboardButton[]{ new KeyboardButton("Оформить заказ") }
        ).resizeKeyboard(true);
        bot.execute(new SendMessage(chatId, "Выберите категорию:\nТак же можете узнать детали заказа по запросу в чат.").replyMarkup(menu));
    }

    private void sendSubCategoryMenu(long chatId, Long parentId) {
        List<Category> subs = chatOrderService.listCategories(parentId);
        KeyboardButton[] buttons = subs.stream().map(c -> new KeyboardButton(c.getName()))
                .toArray(KeyboardButton[]::new);
        Keyboard menu = new ReplyKeyboardMarkup(
                buttons,
                new KeyboardButton[]{ new KeyboardButton("Оформить заказ") },
                new KeyboardButton[]{ new KeyboardButton("В основное меню") }
        ).resizeKeyboard(true);
        bot.execute(new SendMessage(chatId, "Выберите подкатегорию:").replyMarkup(menu));
    }

    private void sendProductList(long chatId, Long categoryId, String categoryName) {
        List<Product> products = chatOrderService.listProducts(categoryId);
        StringBuilder sb = new StringBuilder(categoryName).append("\n");
        List<InlineKeyboardButton[]> rows = new ArrayList<>();
        for (Product p : products) {
            sb.append(p.getName()).append(": Цена: ").append(p.getPrice()).append(" руб.\n");
            rows.add(new InlineKeyboardButton[]{
                    new InlineKeyboardButton(p.getName() + ": Цена " + p.getPrice() + " руб.")
                            .callbackData("product:" + p.getId())
            });
        }
        InlineKeyboardMarkup inline = new InlineKeyboardMarkup(rows.toArray(new InlineKeyboardButton[0][]));
        bot.execute(new SendMessage(chatId, sb.toString()).replyMarkup(inline));
    }
}