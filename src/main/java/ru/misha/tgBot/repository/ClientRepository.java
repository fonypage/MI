package ru.misha.tgBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.misha.tgBot.model.Client;

import java.util.List;
import java.util.Optional;

@RepositoryRestResource(collectionResourceRel="clients", path="clients")
public interface ClientRepository extends JpaRepository<Client, Long> {
    Client findByExternalId(Integer externalId);
    List<Client> findByFullNameContainingIgnoreCase(String name);
    Optional<Client> findByChatId(Long chatId);
}

