package ru.misha.tgBot.controller;

import org.springframework.http.ResponseEntity;
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

    @GetMapping("/search")
    public ResponseEntity<List<Client>> search(@RequestParam("name") String name) {
        List<Client> result = service.searchClientsByName(name);
        return ResponseEntity.ok(result);
    }
}

