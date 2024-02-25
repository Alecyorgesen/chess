package handler;

import com.google.gson.Gson;
import response.Response;
import service.*;
import spark.*;
import model.*;
public class RegisterHandler {
    public final RegisterService registerService;
    public RegisterHandler() {
        this.registerService = new RegisterService();
    }

    public Object register(Request request, spark.Response response) {
        var userData = new Gson().fromJson(request.body(), UserData.class);
        AuthData authData = registerService.register(userData);
        return new Gson().toJson(authData);
    }
}
