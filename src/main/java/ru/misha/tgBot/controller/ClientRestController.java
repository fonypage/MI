package ru.misha.tgBot.controller;

import org.springframework.web.bind.annotation.*;
import ru.misha.tgBot.model.Client;
import ru.misha.tgBot.model.ClientOrder;
import ru.misha.tgBot.model.Product;
import ru.misha.tgBot.service.EntitiesService;

import java.util.List;

@RestController
@RequestMapping("/rest/clients")
public class ClientRestController {

    private final EntitiesService service;

    public ClientRestController(EntitiesService service) {
        this.service = service;
    }

    @GetMapping("/{id}/orders")
    public List<ClientOrder> orders(@PathVariable Long id) {
        return service.getClientOrders(id);
    }

    @GetMapping("/{id}/products")
    public List<Product> products(@PathVariable Long id) {
        return service.getClientProducts(id);
    }

    // доп. задание
    @GetMapping("/search")
    public List<Client> search(@RequestParam String name) {
        return service.searchClientsByName(name);
    }
}

