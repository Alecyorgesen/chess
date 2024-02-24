package server;
import com.google.gson.reflect.TypeToken;
import handler.*;
import response.*;
import model.*;
import service.RegisterService;
import spark.*;
import com.google.gson.Gson;

import java.io.Reader;

public class Server {
    public final RegisterHandler registerHandler;

    public Server() {
        registerHandler = new RegisterHandler();
    }

    public int run(int desiredPort) {
        Spark.port(desiredPort);

        Spark.staticFiles.location("web");

        // Register your endpoints and handle exceptions here.

//        Register Method
        Spark.post("/user", registerHandler::register);

        Spark.awaitInitialization();
        return Spark.port();
    }

    public void stop() {
        Spark.stop();
        Spark.awaitStop();
    }
}
