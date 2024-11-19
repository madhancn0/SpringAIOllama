package mmm.tools.springaidemo.chat;

import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ChatRequestHandler {

    private static final String PROMPT_CONVERSATION_HISTORY_INSTRUCTIONS = """        
    The object `conversational_history` below represents the past interaction between the user and you (the LLM).
    Each `history_entry` is represented as a pair of `prompt` and `response`.
    `prompt` is a past user prompt and `response` was your response for that `prompt`.
        
    Use the information in `conversational_history` if you need to recall things from the conversation
    , or in other words, if the `user_main_prompt` needs any information from past `prompt` or `response`.
    If you don't need the `conversational_history` information, simply respond to the prompt with your built-in knowledge.
                
    `conversational_history`:
        
""";
    private final static Map<String, List<HistoryEntry>> conversationalHistoryStorage = new HashMap<>();
    private static final String PROMPT_GENERAL_INSTRUCTIONS = "Your name is Mike and work as a Bank Teller.Restrict your respone to not more than 15 words";

    private static final String CURRENT_PROMPT_INSTRUCTIONS = """
        
        Here's the `user_main_prompt`:
        
        
        """;

    @Qualifier("ollamaChatModel")
    private final OllamaChatModel ollamaChatClient;

    public ChatRequestHandler(OllamaChatModel ollamaChatClient) {
        this.ollamaChatClient = ollamaChatClient;
    }

    public String call(String userMessage, String historyId) {
        var currentHistory = conversationalHistoryStorage.computeIfAbsent(historyId, k -> new ArrayList<>());

        var historyPrompt = new StringBuilder(PROMPT_CONVERSATION_HISTORY_INSTRUCTIONS);
        currentHistory.forEach(entry -> historyPrompt.append(entry.toString()));

        var contextSystemMessage = new SystemMessage(historyPrompt.toString());
        var generalInstructionsSystemMessage = new SystemMessage(PROMPT_GENERAL_INSTRUCTIONS);
        var currentPromptMessage = new UserMessage(CURRENT_PROMPT_INSTRUCTIONS.concat(userMessage));

        var prompt = new Prompt(List.of(generalInstructionsSystemMessage, contextSystemMessage, currentPromptMessage));
        var response = ollamaChatClient.call(prompt).getResult().getOutput().getContent();
        var contextHistoryEntry = new HistoryEntry(userMessage, response);
        currentHistory.add(contextHistoryEntry);

        return response;
    }
}
