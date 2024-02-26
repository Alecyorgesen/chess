package handler;

import com.google.gson.Gson;
import error.AlreadyTakenException;
import error.BadRequestException;
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
        try {
            var userData = new Gson().fromJson(request.body(), UserData.class);
            var authData = registerService.register(userData);
            response.status(200);
            return new Gson().toJson(authData);
        } catch (BadRequestException exception) {
            response.status(400);
            return new Gson().toJson(new ErrorMessage(exception.getMessage()));
        } catch (AlreadyTakenException exception) {
            response.status(403);
            return new Gson().toJson(new ErrorMessage(exception.getMessage()));
        } catch (Exception exception) {
            response.status(500);
            return new Gson().toJson(new ErrorMessage(exception.getMessage()));
        }
    }
}
