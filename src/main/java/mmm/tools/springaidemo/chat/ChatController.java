package mmm.tools.springaidemo.chat;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/ollama")
public class ChatController {

    private final ChatRequestHandler requestHandler;

    public ChatController(ChatRequestHandler requestHandler) {
        this.requestHandler = requestHandler;
    }


    @PostMapping("/chat")
    public ResponseEntity<LlamaResponse> chat(@RequestBody LlamaRequest helpDeskRequest) {
        var chatResponse = requestHandler.call(helpDeskRequest.getPromptMessage(), helpDeskRequest.getHistoryId());

        return new ResponseEntity<>(new LlamaResponse(chatResponse), HttpStatus.OK);
    }


}
