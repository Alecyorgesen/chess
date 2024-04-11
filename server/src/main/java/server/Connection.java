package server;

import com.google.gson.Gson;
import dataAccess.AuthDAO;
import dataAccess.AuthSQLDAO;
import message.ErrorMessage;
import model.AuthData;
import org.eclipse.jetty.websocket.api.*;

import java.util.Objects;

public class Connection {
    AuthDAO authDAO = new AuthSQLDAO();
    String authToken;
    Session session;
    public Connection(String authToken, Session session) {
        this.authToken = authToken;
        this.session = session;
    }

    public Session getSession() {
        return session;
    }

    public String getAuthToken() {
        return authToken;
    }

    public Connection getConnection(String authToken, Session session) {
        try {
            AuthData authData = authDAO.getAuthUsingAuth(authToken);
            if (authData == null) {
                return null;
            } else {
                return this;
            }
        } catch (Exception ex) {
            sendError(session, ex.getMessage());
        }
        return null;
    }
    static public void sendError(Session session, String message) {
        try {
            ErrorMessage errorMessage = new ErrorMessage(message);
            String json = new Gson().toJson(errorMessage);
            session.getRemote().sendString(json);
        } catch (Exception ex) {
            System.out.println("Something really isn't right: " + ex.getMessage());
        }
    }
    public void send(String message) {
        try {
            this.session.getRemote().sendString(message);
        } catch (Exception ex) {
            sendError(session,ex.getMessage());
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Connection that = (Connection) o;
        return Objects.equals(authToken, that.authToken);
    }

    @Override
    public int hashCode() {
        return Objects.hash(authToken);
    }
}
