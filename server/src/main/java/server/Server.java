package server;
import handler.*;
import spark.*;

public class Server {
    public final RegisterHandler registerHandler;
    public final ClearHandler clearHandler;

    public Server() {
        registerHandler = new RegisterHandler();
        clearHandler = new ClearHandler();
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.externalStaticFileLocation("server/src/main/resources/web");

        // Register your endpoints and handle exceptions here.

//        Spark.get("/one", (req, res) -> {System.out.println("get"); return 1;});
        Spark.post("/user", registerHandler::register);
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
