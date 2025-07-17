package ru.misha.tgBot;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.misha.tgBot.model.*;
import ru.misha.tgBot.repository.*;

import java.util.List;

@Component
public class DbInitializer implements CommandLineRunner {

    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private ProductRepository productRepository;
    @Autowired
    private ClientRepository clientRepository;
    @Autowired
    private ClientOrderRepository clientOrderRepository;
    @Autowired
    private OrderProductRepository orderProductRepository;

    @Override
    @Transactional
    public void run(String... args) {
        // --- 1) Категории ---
        if (categoryRepository.count() == 0) {
            // Родительские
            Category pizza = new Category();
            pizza.setName("Пицца");
            categoryRepository.save(pizza);

            Category rolls = new Category();
            rolls.setName("Роллы");
            categoryRepository.save(rolls);

            Category burgers = new Category();
            burgers.setName("Бургеры");
            categoryRepository.save(burgers);

            Category drinks = new Category();
            drinks.setName("Напитки");
            categoryRepository.save(drinks);

            // --- Подкатегории Пиццы ---
            Category classicPizza = new Category();
            classicPizza.setName("Классические пиццы");
            classicPizza.setParent(pizza);
            categoryRepository.save(classicPizza);

            Category signaturePizza = new Category();
            signaturePizza.setName("Фирменные пиццы");
            signaturePizza.setParent(pizza);
            categoryRepository.save(signaturePizza);

            Category spicyPizza = new Category();
            spicyPizza.setName("Острые пиццы");
            spicyPizza.setParent(pizza);
            categoryRepository.save(spicyPizza);

            // --- Подкатегории Роллы ---
            Category classicRolls = new Category();
            classicRolls.setName("Классические роллы");
            classicRolls.setParent(rolls);
            categoryRepository.save(classicRolls);

            Category bakedRolls = new Category();
            bakedRolls.setName("Запеченные роллы");
            bakedRolls.setParent(rolls);
            categoryRepository.save(bakedRolls);

            Category sweetRolls = new Category();
            sweetRolls.setName("Сладкие роллы");
            sweetRolls.setParent(rolls);
            categoryRepository.save(sweetRolls);

            Category sets = new Category();
            sets.setName("Наборы");
            sets.setParent(rolls);
            categoryRepository.save(sets);

            // --- Подкатегории Бургеры ---
            Category classicBurgers = new Category();
            classicBurgers.setName("Классические бургеры");
            classicBurgers.setParent(burgers);
            categoryRepository.save(classicBurgers);

            Category spicyBurgers = new Category();
            spicyBurgers.setName("Острые бургеры");
            spicyBurgers.setParent(burgers);
            categoryRepository.save(spicyBurgers);

            // --- Подкатегории Напитки ---
            Category softDrinks = new Category();
            softDrinks.setName("Газированные напитки");
            softDrinks.setParent(drinks);
            categoryRepository.save(softDrinks);

            Category energyDrinks = new Category();
            energyDrinks.setName("Энергетические напитки");
            energyDrinks.setParent(drinks);
            categoryRepository.save(energyDrinks);

            Category juices = new Category();
            juices.setName("Соки");
            juices.setParent(drinks);
            categoryRepository.save(juices);

            Category others = new Category();
            others.setName("Другие");
            others.setParent(drinks);
            categoryRepository.save(others);

            // --- 2) Товары по ВСЕМ подкатегориям ---
            if (productRepository.count() == 0) {
                // Пицца - подкатегории
                for (int i = 1; i <= 3; i++) {
                    Product p = new Product();
                    p.setName("Классическая пицца " + i);
                    p.setDescription("Описание классической пиццы №" + i);
                    p.setPrice(10.0 + i);
                    p.setCategory(classicPizza);
                    productRepository.save(p);
                }
                for (int i = 1; i <= 3; i++) {
                    Product p = new Product();
                    p.setName("Фирменная пицца " + i);
                    p.setDescription("Описание фирменной пиццы №" + i);
                    p.setPrice(12.0 + i);
                    p.setCategory(signaturePizza);
                    productRepository.save(p);
                }
                for (int i = 1; i <= 3; i++) {
                    Product p = new Product();
                    p.setName("Острая пицца " + i);
                    p.setDescription("Описание острой пиццы №" + i);
                    p.setPrice(11.0 + i);
                    p.setCategory(spicyPizza);
                    productRepository.save(p);
                }

                // Роллы - подкатегории
                for (int i = 1; i <= 3; i++) {
                    Product p = new Product();
                    p.setName("Классический ролл " + i);
                    p.setDescription("Классический ролл №" + i);
                    p.setPrice(6.0 + i);
                    p.setCategory(classicRolls);
                    productRepository.save(p);
                }
                for (int i = 1; i <= 3; i++) {
                    Product p = new Product();
                    p.setName("Запечённый ролл " + i);
                    p.setDescription("Запечённый ролл №" + i);
                    p.setPrice(7.0 + i);
                    p.setCategory(bakedRolls);
                    productRepository.save(p);
                }
                for (int i = 1; i <= 3; i++) {
                    Product p = new Product();
                    p.setName("Сладкий ролл " + i);
                    p.setDescription("Сладкий ролл №" + i);
                    p.setPrice(8.0 + i);
                    p.setCategory(sweetRolls);
                    productRepository.save(p);
                }
                for (int i = 1; i <= 3; i++) {
                    Product p = new Product();
                    p.setName("Набор роллов " + i);
                    p.setDescription("Набор роллов №" + i);
                    p.setPrice(12.0 + i);
                    p.setCategory(sets);
                    productRepository.save(p);
                }

                // Бургеры - подкатегории
                for (int i = 1; i <= 3; i++) {
                    Product p = new Product();
                    p.setName("Классический бургер " + i);
                    p.setDescription("Описание классического бургера №" + i);
                    p.setPrice(8.0 + i);
                    p.setCategory(classicBurgers);
                    productRepository.save(p);
                }
                for (int i = 1; i <= 3; i++) {
                    Product p = new Product();
                    p.setName("Острый бургер " + i);
                    p.setDescription("Описание острого бургера №" + i);
                    p.setPrice(9.0 + i);
                    p.setCategory(spicyBurgers);
                    productRepository.save(p);
                }

                // Напитки - подкатегории
                for (int i = 1; i <= 3; i++) {
                    Product p = new Product();
                    p.setName("Газировка " + i);
                    p.setDescription("Описание газированного напитка №" + i);
                    p.setPrice(2.0 + 0.5 * i);
                    p.setCategory(softDrinks);
                    productRepository.save(p);
                }
                for (int i = 1; i <= 3; i++) {
                    Product p = new Product();
                    p.setName("Энергетик " + i);
                    p.setDescription("Описание энергетического напитка №" + i);
                    p.setPrice(3.0 + 0.5 * i);
                    p.setCategory(energyDrinks);
                    productRepository.save(p);
                }
                for (int i = 1; i <= 3; i++) {
                    Product p = new Product();
                    p.setName("Сок " + i);
                    p.setDescription("Описание сока №" + i);
                    p.setPrice(3.5 + 0.5 * i);
                    p.setCategory(juices);
                    productRepository.save(p);
                }
                for (int i = 1; i <= 3; i++) {
                    Product p = new Product();
                    p.setName("Другой напиток " + i);
                    p.setDescription("Описание другого напитка №" + i);
                    p.setPrice(2.5 + 0.5 * i);
                    p.setCategory(others);
                    productRepository.save(p);
                }
            }
        }

        // --- 3) Клиенты ---
        if (clientRepository.count() == 0) {
            Client client1 = new Client();
            client1.setExternalId(1L);
            client1.setFullName("Иванов Иван Иванович");
            client1.setAddress("ул. Ленина, д.42, кв.15, г. Новосибирск");
            client1.setPhoneNumber("9000000000");
            client1.setChatId(123456789L);
            clientRepository.save(client1);

            Client client2 = new Client();
            client2.setExternalId(2L);
            client2.setFullName("Петров Петр Петрович");
            client2.setAddress("ул. Правды, д.22, кв.1, г. Красноярск");
            client2.setPhoneNumber("9000000001");
            client2.setChatId(987654321L);
            clientRepository.save(client2);

            Client client3 = new Client();
            client3.setExternalId(3L);
            client3.setFullName("Сидоров Сидр Сидорович");
            client3.setAddress("ул. Кирова, д.19, кв.45, г. Майкоп");
            client3.setPhoneNumber("9000000002");
            client3.setChatId(456123789L);
            clientRepository.save(client3);
        }

        // --- 4) Заказы и позиции ---
        if (clientOrderRepository.count() == 0 && productRepository.count() > 0 && clientRepository.count() > 0) {
            List<Client> clients = clientRepository.findAll();
            List<Product> products = productRepository.findAll();

            if (!clients.isEmpty()) {
                ClientOrder order1 = new ClientOrder();
                order1.setClient(clients.get(0));
                order1.setStatus(1);
                order1.setTotal(0.0);
                clientOrderRepository.save(order1);

                ClientOrder order2 = new ClientOrder();
                order2.setClient(clients.get(1));
                order2.setStatus(2);
                order2.setTotal(0.0);
                clientOrderRepository.save(order2);

                // позиции к заказу
                if (products.size() >= 3) {
                    OrderProduct op1 = new OrderProduct();
                    op1.setClientOrder(order1);
                    op1.setProduct(products.get(0));
                    op1.setCountProduct(2);
                    orderProductRepository.save(op1);

                    OrderProduct op2 = new OrderProduct();
                    op2.setClientOrder(order1);
                    op2.setProduct(products.get(1));
                    op2.setCountProduct(1);
                    orderProductRepository.save(op2);

                    OrderProduct op3 = new OrderProduct();
                    op3.setClientOrder(order2);
                    op3.setProduct(products.get(2));
                    op3.setCountProduct(3);
                    orderProductRepository.save(op3);

                    // обновляем total
                    double total1 = op1.getProduct().getPrice() * op1.getCountProduct()
                            + op2.getProduct().getPrice() * op2.getCountProduct();
                    order1.setTotal(total1);
                    clientOrderRepository.save(order1);
                }
            }
        }
    }
}