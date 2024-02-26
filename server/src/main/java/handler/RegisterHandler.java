package handler;

import com.google.gson.Gson;
import message.ErrorMessage;
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
        var object = registerService.register(userData);
        if (object.getClass().equals(AuthData.class)) {
            response.status(200);
        } else {
            ErrorMessage errorMessage = (ErrorMessage) object;
            if (errorMessage.message().equals("Error: bad request")) {
                response.status(400);
            } else if (errorMessage.message().equals("Error: already taken")) {
                response.status(403);
            } else {
                response.status(500);
            }
        }
        return new Gson().toJson(object);
    }
}
