package handler;

import com.google.gson.Gson;
import org.eclipse.jetty.server.Authentication;
import response.RegisterResponse;
import service.*;
import spark.*;
import model.*;
public class RegisterHandler {
    public final RegisterService registerService;
    public RegisterHandler() {
        this.registerService = new RegisterService();
    }

    public RegisterResponse register(Request request, Response response) {
        var userData = new Gson().fromJson(request.body(), UserData.class);
        return registerService.register(userData);
    }
}
