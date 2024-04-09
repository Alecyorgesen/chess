package webSocketMessages.serverMessages;

public class Notification extends ServerMessage {
    String message;
    public Notification(ServerMessageType serverMessage, String message) {
        super(serverMessage);
        this.serverMessageType = ServerMessageType.NOTIFICATION;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
