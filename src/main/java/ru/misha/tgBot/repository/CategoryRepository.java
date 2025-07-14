package ru.misha.tgBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.misha.tgBot.model.Category;

@RepositoryRestResource(path = "categories", collectionResourceRel = "categories")
public interface CategoryRepository extends JpaRepository<Category, Long> {
}
