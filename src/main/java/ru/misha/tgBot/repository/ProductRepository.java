package ru.misha.tgBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.misha.tgBot.model.Product;

import java.util.Optional;

@RepositoryRestResource(path = "products", collectionResourceRel = "products")
public interface ProductRepository extends JpaRepository<Product, Long> {
}
