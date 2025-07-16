package ru.misha.tgBot.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.misha.tgBot.model.Client;
import ru.misha.tgBot.model.ClientOrder;
import ru.misha.tgBot.model.Product;
import ru.misha.tgBot.repository.ClientOrderRepository;
import ru.misha.tgBot.repository.ClientRepository;
import ru.misha.tgBot.repository.OrderProductRepository;
import ru.misha.tgBot.repository.ProductRepository;

import java.util.List;

@Service
@Transactional
public class ClientServiceImpl implements ClientService {

    private final ClientOrderRepository orderRepo;
    private final OrderProductRepository opRepo;
    private final ClientRepository clientRepo;
    private final ProductRepository productRepo;

    public ClientServiceImpl(ClientOrderRepository orderRepo,
                             OrderProductRepository opRepo,
                             ClientRepository clientRepo,
                             ProductRepository productRepo) {
        this.orderRepo  = orderRepo;
        this.opRepo     = opRepo;
        this.clientRepo = clientRepo;
        this.productRepo = productRepo;
    }

    @Override
    public List<ClientOrder> getClientOrders(Long clientId) {
        return orderRepo.findByClient_Id(clientId);
    }

    @Override
    public List<Product> getClientProducts(Long clientId) {
        return productRepo.findDistinctProductsByClientId(clientId);
    }

    @Override
    public List<Client> searchClientsByName(String name) {
        return clientRepo.findByFullNameContainingIgnoreCase(name);
    }
}