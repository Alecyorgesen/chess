package server;
import handler.*;
import spark.*;

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

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.externalStaticFileLocation("server/src/main/resources/web");

        // Register your endpoints and handle exceptions here.

//        Spark.get("/", (req, res) -> {System.out.println("get"); return 1;});
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
