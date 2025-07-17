package ru.misha.tgBot.OpenAI;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class OpenAiService {
    private final ChatClient chatClient;
    private final ToolService toolService;

    private static final String SYSTEM_PROMPT = "Вы — помощник для работы с заказами и обратной связью";

    public OpenAiService(ChatClient.Builder builder,
                         ToolService toolService) {
        this.chatClient = builder.build();
        this.toolService = toolService;
    }

    public String sendMessage(List<Message> history, String userInput) {
        // просто цепляем history и tools и возвращаем строку
        return chatClient.prompt()
                .system(SYSTEM_PROMPT)
                .user(userInput)
                .messages(history)
                .tools(toolService)
                .call()
                .content();
    }
}
