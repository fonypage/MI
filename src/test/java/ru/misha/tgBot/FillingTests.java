//package ru.misha.tgBot;
//
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//
//
//import ru.misha.tgBot.model.*;
//import ru.misha.tgBot.repository.CategoryRepository;
//import ru.misha.tgBot.repository.ProductRepository;
//import ru.misha.tgBot.repository.ClientRepository;
//import ru.misha.tgBot.repository.ClientOrderRepository;
//import ru.misha.tgBot.repository.OrderProductRepository;
//
//@SpringBootTest
//public class FillingTests {
//
//    @Autowired
//    private CategoryRepository categoryRepository;
//    @Autowired
//    private ProductRepository productRepository;
//    @Autowired
//    private ClientRepository clientRepository;
//    @Autowired
//    private ClientOrderRepository clientOrderRepository;
//    @Autowired
//    private OrderProductRepository orderProductRepository;
//
//    @Test
//    public void fillAllData() {
//        // 1) создаём корневые категории
//        Category pizza = new Category();
//        pizza.setName("Пицца");
//        categoryRepository.save(pizza);
//
//        Category rolls = new Category();
//        rolls.setName("Роллы");
//        categoryRepository.save(rolls);
//
//        Category burgers = new Category();
//        burgers.setName("Бургеры");
//        categoryRepository.save(burgers);
//
//        Category drinks = new Category();
//        drinks.setName("Напитки");
//        categoryRepository.save(drinks);
//
//        // 2) создаём подкатегории «Роллы»
//        Category classicRolls = new Category();
//        classicRolls.setName("Классические роллы");
//        classicRolls.setParent(rolls);
//        categoryRepository.save(classicRolls);
//
//        Category bakedRolls = new Category();
//        bakedRolls.setName("Запеченные роллы");
//        bakedRolls.setParent(rolls);
//        categoryRepository.save(bakedRolls);
//
//        Category sweetRolls = new Category();
//        sweetRolls.setName("Сладкие роллы");
//        sweetRolls.setParent(rolls);
//        categoryRepository.save(sweetRolls);
//
//        Category sets = new Category();
//        sets.setName("Наборы");
//        sets.setParent(rolls);
//        categoryRepository.save(sets);
//
//        // 3) подкатегории «Бургеры»
//        Category classicBurgers = new Category();
//        classicBurgers.setName("Классические бургеры");
//        classicBurgers.setParent(burgers);
//        categoryRepository.save(classicBurgers);
//
//        Category spicyBurgers = new Category();
//        spicyBurgers.setName("Острые бургеры");
//        spicyBurgers.setParent(burgers);
//        categoryRepository.save(spicyBurgers);
//
//        // 4) подкатегории «Напитки»
//        Category softDrinks = new Category();
//        softDrinks.setName("Газированные напитки");
//        softDrinks.setParent(drinks);
//        categoryRepository.save(softDrinks);
//
//        Category energyDrinks = new Category();
//        energyDrinks.setName("Энергетические напитки");
//        energyDrinks.setParent(drinks);
//        categoryRepository.save(energyDrinks);
//
//        Category juices = new Category();
//        juices.setName("Соки");
//        juices.setParent(drinks);
//        categoryRepository.save(juices);
//
//        Category others = new Category();
//        others.setName("Другие");
//        others.setParent(drinks);
//        categoryRepository.save(others);
//
//        // 5) товары — по 3 штуки в каждой подкатегории
//        // для sweetRolls
//        for (int i = 1; i <= 3; i++) {
//            Product p = new Product();
//            p.setName("Сладкий ролл " + i);
//            p.setDescription("Описание сладкого ролла №" + i);
//            p.setPrice(7.0 + i);
//            p.setCategory(sweetRolls);
//            productRepository.save(p);
//        }
//        // для sets
//        for (int i = 1; i <= 3; i++) {
//            Product p = new Product();
//            p.setName("Набор роллов " + i);
//            p.setDescription("Описание набора роллов №" + i);
//            p.setPrice(12.0 + i);
//            p.setCategory(sets);
//            productRepository.save(p);
//        }
//        // для classicBurgers
//        for (int i = 1; i <= 3; i++) {
//            Product p = new Product();
//            p.setName("Классический бургер " + i);
//            p.setDescription("Описание классического бургера №" + i);
//            p.setPrice(8.0 + i);
//            p.setCategory(classicBurgers);
//            productRepository.save(p);
//        }
//        // для spicyBurgers
//        for (int i = 1; i <= 3; i++) {
//            Product p = new Product();
//            p.setName("Острый бургер " + i);
//            p.setDescription("Описание острого бургера №" + i);
//            p.setPrice(9.0 + i);
//            p.setCategory(spicyBurgers);
//            productRepository.save(p);
//        }
//        // для softDrinks
//        for (int i = 1; i <= 3; i++) {
//            Product p = new Product();
//            p.setName("Газированный напиток " + i);
//            p.setDescription("Описание газированного напитка №" + i);
//            p.setPrice(2.0 + 0.5 * i);
//            p.setCategory(softDrinks);
//            productRepository.save(p);
//        }
//        // для energyDrinks
//        for (int i = 1; i <= 3; i++) {
//            Product p = new Product();
//            p.setName("Энергетический напиток " + i);
//            p.setDescription("Описание энергетического напитка №" + i);
//            p.setPrice(3.0 + 0.5 * i);
//            p.setCategory(energyDrinks);
//            productRepository.save(p);
//        }
//        // для juices
//        for (int i = 1; i <= 3; i++) {
//            Product p = new Product();
//            p.setName("Сок " + i);
//            p.setDescription("Описание сока №" + i);
//            p.setPrice(3.5 + 0.5 * i);
//            p.setCategory(juices);
//            productRepository.save(p);
//        }
//        // для others
//        for (int i = 1; i <= 3; i++) {
//            Product p = new Product();
//            p.setName("Другой напиток " + i);
//            p.setDescription("Описание другого напитка №" + i);
//            p.setPrice(2.5 + 0.5 * i);
//            p.setCategory(others);
//            productRepository.save(p);
//        }
//
//        // 6) клиенты
//        Client client1 = new Client();
//        client1.setExternalId(1L);
//        client1.setFullName("Иванов Иван Иванович");
//        client1.setAddress("ул. Ленина, д.42, кв.15, г. Новосибирск");
//        client1.setPhoneNumber("9000000000");
//        clientRepository.save(client1);
//
//        Client client2 = new Client();
//        client2.setExternalId(2L);
//        client2.setFullName("Петров Петр Петрович");
//        client2.setAddress("ул. Правды, д.22, кв.1, г. Красноярск");
//        client2.setPhoneNumber("9000000001");
//        clientRepository.save(client2);
//
//        Client client3 = new Client();
//        client3.setExternalId(3L);
//        client3.setFullName("Сидоров Сидр Сидорович");
//        client3.setAddress("ул. Кирова, д.19, кв.45, г. Майкоп");
//        client3.setPhoneNumber("9000000002");
//        clientRepository.save(client3);
//
//        // 7) заказы
//        ClientOrder order1 = new ClientOrder();
//        order1.setClient(client1);
//        order1.setStatus(1);
//        order1.setTotal(0.0);
//        clientOrderRepository.save(order1);
//
//        ClientOrder order2 = new ClientOrder();
//        order2.setClient(client2);
//        order2.setStatus(2);
//        order2.setTotal(0.0);
//        clientOrderRepository.save(order2);
//
//        // 8) связи заказ–товар
//        OrderProduct op1 = new OrderProduct();
//        op1.setClientOrder(order1);
//        op1.setProduct(productRepository.findAll().get(0)); // первый продукт из списка
//        op1.setCountProduct(2);
//        orderProductRepository.save(op1);
//
//        OrderProduct op2 = new OrderProduct();
//        op2.setClientOrder(order1);
//        op2.setProduct(productRepository.findAll().get(1)); // второй продукт
//        op2.setCountProduct(1);
//        orderProductRepository.save(op2);
//
//        OrderProduct op3 = new OrderProduct();
//        op3.setClientOrder(order2);
//        op3.setProduct(productRepository.findAll().get(2)); // третий продукт
//        op3.setCountProduct(3);
//        orderProductRepository.save(op3);
//
//        // 9) пересчёт total у заказа №1
//        double total1 = op1.getProduct().getPrice() * op1.getCountProduct()
//                + op2.getProduct().getPrice() * op2.getCountProduct();
//        order1.setTotal(total1);
//        clientOrderRepository.save(order1);
//    }
//}
