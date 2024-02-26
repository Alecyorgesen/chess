package handler;

import com.google.gson.Gson;
import error.UnauthorizedException;
import message.ErrorMessage;
import service.LogoutService;
import spark.Request;
import spark.Response;
import java.util.Map;

public class LogoutHandler {
        static final LogoutService logoutService = new LogoutService();
    public LogoutHandler() {

    }

    public Object logout(Request request, Response response) {
        try {
            logoutService.logout(request.headers("Authorization"));
            response.status(200);
            return "{}";
        } catch (UnauthorizedException exception) {
            response.status(401);
            return new Gson().toJson(new ErrorMessage(exception.getMessage()));
        } catch (Exception exception) {
            response.status(500);
            return new Gson().toJson(new ErrorMessage(exception.getMessage()));
        }
    }
}
