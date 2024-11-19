package mmm.tools.springaidemo.chat;

public class HistoryEntry {

    private String prompt;

    private String response;

    public HistoryEntry(String prompt, String response) {
        this.prompt = prompt;
        this.response = response;
    }

    @Override
    public String toString() {
        return String.format("""
                        `history_entry`:
                            `prompt`: %s
                        
                            `response`: %s
                        -----------------
                       \n
            """, prompt, response);
    }

}
