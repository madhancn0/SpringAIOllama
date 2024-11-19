package mmm.tools.springaidemo.chat;

import com.fasterxml.jackson.annotation.JsonProperty;

public class LlamaRequest {

    @JsonProperty("prompt_message")
    String promptMessage;

    @JsonProperty("history_id")
    String historyId;

    public String getPromptMessage() {
        return promptMessage;
    }

    public void setPromptMessage(String promptMessage) {
        this.promptMessage = promptMessage;
    }

    public String getHistoryId() {
        return historyId;
    }

    public void setHistoryId(String historyId) {
        this.historyId = historyId;
    }
}
