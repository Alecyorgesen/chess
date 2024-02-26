package handler;

import com.google.gson.Gson;
import error.UnauthorizedException;
import message.ErrorMessage;
import model.AuthData;
import model.UserData;
import service.LoginService;
import spark.Request;
import spark.Response;

public class LoginHandler {
    static final LoginService loginService = new LoginService();
    public LoginHandler() {

    }

    public Object login(Request request, Response response) {
        try {
            var userData = new Gson().fromJson(request.body(), UserData.class);
            AuthData authData = loginService.login(userData);
            response.status(200);
            return new Gson().toJson(authData);
        } catch (UnauthorizedException exception) {
            response.status(401);
            return new Gson().toJson(new ErrorMessage(exception.getMessage()));
        } catch (Exception exception) {
            return new ErrorMessage(exception.getMessage());
        }
    }
}
