package ru.misha.tgBot.bot;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.CallbackQuery;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.Update;
import com.pengrad.telegrambot.model.request.*;
import com.pengrad.telegrambot.request.AnswerCallbackQuery;
import com.pengrad.telegrambot.request.SendMessage;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.misha.tgBot.model.Category;
import ru.misha.tgBot.model.Client;
import ru.misha.tgBot.model.ClientOrder;
import ru.misha.tgBot.model.Product;
import ru.misha.tgBot.service.ChatOrderService;
import ru.misha.tgBot.service.dto.OrderSummary;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class TelegramBotConnection {

    private final TelegramBot bot;
    private final ChatOrderService chatOrderService;

    public TelegramBotConnection(@Value("${telegram.bot.token}") String token,
                                 ChatOrderService chatOrderService) {
        this.bot = new TelegramBot(token);
        this.chatOrderService = chatOrderService;
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
        } else if (update.message() != null) {
            handleMessage(update.message());
        }
    }

    private void handleMessage(Message message) {
        long chatId = message.chat().id();
        String text = (message.text() != null) ? message.text().trim() : "";
        String userName = message.from().firstName();
        Client client = chatOrderService.findOrCreateClient(chatId, userName);
        ClientOrder order = chatOrderService.getOrCreateActiveOrder(client);

        if ("/start".equalsIgnoreCase(text) || "В основное меню".equalsIgnoreCase(text)) {
            sendMainMenu(chatId);
            return;
        }

        // Оформить заказ — только показать корзину и две inline кнопки!
        if ("Оформить заказ".equalsIgnoreCase(text)) {
            OrderSummary summary = chatOrderService.getOrderSummary(order);
            if (summary.getItems().isEmpty()) {
                bot.execute(new SendMessage(chatId, "Ваш заказ пуст!"));
                return;
            }
            // Две кнопки: Заказать (confirm_order) и Главное меню (main_menu)
            InlineKeyboardMarkup inline = new InlineKeyboardMarkup(
                    new InlineKeyboardButton[]{
                            new InlineKeyboardButton("Заказать").callbackData("confirm_order")
                    },
                    new InlineKeyboardButton[]{
                            new InlineKeyboardButton("Главное меню").callbackData("main_menu")
                    }
            );
            bot.execute(new SendMessage(chatId, summary.toString()).replyMarkup(inline));
            return;
        }

        // Выбор категории/подкатегории/товара:
        Optional<Category> selectedCatOpt = chatOrderService.listCategories(null).stream()
                .filter(c -> c.getName().equalsIgnoreCase(text))
                .findFirst();
        if (selectedCatOpt.isPresent()) {
            Category selectedCat = selectedCatOpt.get();
            List<Category> subCategories = chatOrderService.listCategories(selectedCat.getId());
            if (!subCategories.isEmpty()) {
                sendSubCategoryMenu(chatId, selectedCat.getId());
            } else {
                sendProductList(chatId, selectedCat.getId(), selectedCat.getName());
            }
            return;
        }

        List<Category> allSubs = chatOrderService.listCategories(null).stream()
                .flatMap(cat -> chatOrderService.listCategories(cat.getId()).stream())
                .collect(Collectors.toList());
        Optional<Category> subOpt = allSubs.stream()
                .filter(c -> c.getName().equalsIgnoreCase(text))
                .findFirst();
        if (subOpt.isPresent()) {
            sendProductList(chatId, subOpt.get().getId(), subOpt.get().getName());
            return;
        }
    }

    private void handleCallback(CallbackQuery cb) {
        String data = cb.data();
        long chatId = cb.message().chat().id();
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
            String deliveryTime = "90 мин.";
            String confirmText = String.format(
                    "Заказ №%d подтвержден. Курьер уже едет к Вам по адресу %s.\nПриблизительное время доставки %s",
                    orderId, address, deliveryTime
            );
            bot.execute(new SendMessage(chatId, confirmText));
            // Создать новый активный заказ для клиента (если нужно, зависит от логики)
            chatOrderService.getOrCreateActiveOrder(client);
            sendMainMenu(chatId);
        }
    }

    private void sendMainMenu(long chatId) {
        Keyboard mainMenu = new ReplyKeyboardMarkup(
                new KeyboardButton[] {
                        new KeyboardButton("Пицца"),
                        new KeyboardButton("Роллы"),
                        new KeyboardButton("Бургеры"),
                        new KeyboardButton("Напитки")
                },
                new KeyboardButton[] {
                        new KeyboardButton("Оформить заказ")
                }
        ).resizeKeyboard(true);
        bot.execute(new SendMessage(chatId, "Выберите категорию:").replyMarkup(mainMenu));
    }

    private void sendSubCategoryMenu(long chatId, Long parentId) {
        List<Category> subCats = chatOrderService.listCategories(parentId);
        KeyboardButton[] row = subCats.stream()
                .map(c -> new KeyboardButton(c.getName()))
                .toArray(KeyboardButton[]::new);
        Keyboard subMenu = new ReplyKeyboardMarkup(
                row,
                new KeyboardButton[] { new KeyboardButton("Оформить заказ") },
                new KeyboardButton[] { new KeyboardButton("В основное меню") }
        ).resizeKeyboard(true);
        bot.execute(new SendMessage(chatId, "Выберите подкатегорию:").replyMarkup(subMenu));
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