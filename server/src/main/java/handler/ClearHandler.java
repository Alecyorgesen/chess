package handler;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import message.ErrorMessage;
import model.AuthData;
import model.UserData;
import service.ClearService;
import service.RegisterService;
import spark.Request;

public class ClearHandler {
    public final ClearService clearService;
    public ClearHandler() {
        this.clearService = new ClearService();
    }

    public Object clear(Request request, spark.Response response) {
        try {
            clearService.clear();
            response.status(200);
            return "{}";
        } catch (Exception exception) {
            response.status(500);
            return new ErrorMessage(exception.getMessage());
        }
    }
}
