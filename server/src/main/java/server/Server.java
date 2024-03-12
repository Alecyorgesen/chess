package server;
import dataAccess.DataAccessException;
import dataAccess.DatabaseManager;
import handler.*;
import spark.*;

import javax.xml.crypto.Data;

public class Server {
    public final RegisterHandler registerHandler = new RegisterHandler();
    public final ClearHandler clearHandler = new ClearHandler();
    public final LoginHandler loginHandler = new LoginHandler();
    public final LogoutHandler logoutHandler = new LogoutHandler();
    public final ListGamesHandler listGamesHandler = new ListGamesHandler();
    public final CreateGameHandler createGameHandler = new CreateGameHandler();
    public final JoinGameHandler joinGameHandler = new JoinGameHandler();


    public Server() {
    }
    static {
        try {
            DatabaseManager.createDatabase();
        } catch (Exception ex) {
            System.out.println("Database already exists or cannot be created");
        }
    }
    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

        Spark.post("/user", registerHandler::register);
        Spark.post("/session", loginHandler::login);
        Spark.delete("/session", logoutHandler::logout);
        Spark.get("/game", listGamesHandler::listGames);
        Spark.post("/game", createGameHandler::createGame);
        Spark.put("/game", joinGameHandler::joinGame);
        Spark.delete("/db", clearHandler::clear);
        Spark.awaitInitialization();
        System.out.println("Server running on port 8080");
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
