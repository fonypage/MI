package ru.misha.tgBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.misha.tgBot.model.Client;
import ru.misha.tgBot.model.ClientOrder;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(path = "client-orders", collectionResourceRel = "client-orders")
public interface ClientOrderRepository extends JpaRepository<ClientOrder, Long> {
    List<ClientOrder> findByClient_Id(Long clientId);
    Optional<ClientOrder> findByClientAndStatus(Client client, int status);
}
