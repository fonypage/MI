package ru.misha.tgBot.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.misha.tgBot.model.Client;
import ru.misha.tgBot.model.ClientOrder;
import ru.misha.tgBot.model.Product;
import ru.misha.tgBot.service.ClientService;

import java.util.List;

@RestController
@RequestMapping("/rest/clients")
public class ClientRestController {

    private final ClientService clientService;

    public ClientRestController(ClientService clientService) {
        this.clientService = clientService;
    }

    @GetMapping("/{id}/orders")
    public List<ClientOrder> orders(@PathVariable Long id) {
        return clientService.getClientOrders(id);
    }

    @GetMapping("/{id}/products")
    public List<Product> products(@PathVariable Long id) {
        return clientService.getClientProducts(id);
    }

    @GetMapping("/search")
    public ResponseEntity<List<Client>> search(@RequestParam("name") String name) {
        return ResponseEntity.ok(clientService.searchClientsByName(name));
    }
}

