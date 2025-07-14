package ru.misha.tgBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.misha.tgBot.model.ClientOrder;

@RepositoryRestResource(path = "client-orders", collectionResourceRel = "client-orders")
public interface ClientOrderRepository extends JpaRepository<ClientOrder, Long> {
}
