package ru.misha.tgBot.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.misha.tgBot.model.Feedback;

public interface FeedbackRepository extends JpaRepository<Feedback, Long> {
}

