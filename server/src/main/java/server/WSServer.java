package server;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.api.*;
import webSocketMessages.userCommands.*;

import java.util.HashSet;
import java.util.List;

import static webSocketMessages.userCommands.UserGameCommand.CommandType.*;

@WebSocket
public class WSServer {
    static HashSet<Connection> connections = new HashSet<>();
    @OnWebSocketMessage
    public void onMessage(Session session, String msg) throws Exception {
        UserGameCommand command = new Gson().fromJson(msg, UserGameCommand.class);
        Connection connection = new Connection(command.getAuthString(), session);
        connections.add(connection);
        var conn = connection.getConnection(command.getAuthString(), session);
        if (conn != null) {
            switch (command.getCommandType()) {
                case JOIN_PLAYER -> join(conn, command);
                case JOIN_OBSERVER -> observe(conn, command);
                case DRAW_BOARD -> drawBoard(conn, command);
                case MAKE_MOVE -> move(conn, command);
                case LEAVE -> leave(conn, command);
                case RESIGN -> resign(conn, command);
            }
        } else {
            Connection.sendError(session, "unknown user");
        }
    }
    public void join(Connection connection, UserGameCommand userGameCommand) {
        JoinPlayer joinPlayerCommand = (JoinPlayer) userGameCommand;

    }
}