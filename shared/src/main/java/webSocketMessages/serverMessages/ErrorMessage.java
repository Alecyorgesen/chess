package webSocketMessages.serverMessages;

public class ErrorMessage extends ServerMessage {
    String errorMessage;
    public ErrorMessage(ServerMessageType serverMessage, String errorMessage) {
        super(serverMessage);
        this.serverMessageType = ServerMessageType.ERROR;
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }
}
