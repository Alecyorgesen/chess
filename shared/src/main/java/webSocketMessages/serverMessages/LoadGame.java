package webSocketMessages.serverMessages;

public class LoadGame extends ServerMessage {
    Object game;
    public LoadGame(ServerMessageType serverMessage, Object game) {
        super(serverMessage);
        this.serverMessageType = ServerMessageType.LOAD_GAME;
        this.game = game;
    }
}
