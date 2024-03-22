package clientTests;

import client.ServerFacade;
import org.junit.jupiter.api.*;
import server.Server;


public class ServerFacadeTests {

    private static Server server;
    static ServerFacade serverFacade;

    @BeforeAll
    public static void init() {
        server = new Server();
        var port = server.run(0);
        System.out.println("Started test HTTP server on " + port);
        serverFacade = new ServerFacade(port);
        serverFacade.clear();
    }

    @AfterAll
    static void stopServer() {
        server.stop();
    }


    @Test
    public void tests() {
        Assertions.assertTrue(true);
        String username = "lDrac360l";
        String password = "THE PASSWORD";
        String email = "email"
        serverFacade.register(username, password, email);
    }

}
