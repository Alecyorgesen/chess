package webSocketMessages.serverMessages;

public class Error extends ServerMessage {
    String errorMessage;
    public Error(ServerMessageType serverMessage, String errorMessage) {
        super(serverMessage);
        this.serverMessageType = ServerMessageType.ERROR;
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
