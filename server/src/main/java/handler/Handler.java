package handler;

import com.google.gson.Gson;
import error.AlreadyTakenException;
import error.BadRequestException;
import error.UnauthorizedException;
import message.ErrorMessage;
import model.UserData;
import spark.Request;
import spark.Response;

public class Handler {

    public Handler() {

    }

    public Object handle(Request request, Response response) {
//        try {
////            var userData = new Gson().fromJson(request.body(), UserData.class);
////            var authData = registerService.register(userData);
////            response.status(200);
////            return new Gson().toJson(authData);
//        } catch (BadRequestException exception) {
//            response.status(400);
//            return new Gson().toJson(new ErrorMessage(exception.getMessage()));
//        } catch (UnauthorizedException exception) {
//            response.status(401);
//            return new Gson().toJson(new ErrorMessage(exception.getMessage()));
//        } catch (AlreadyTakenException exception) {
//            response.status(403);
//            return new Gson().toJson(new ErrorMessage(exception.getMessage()));
//        } catch (Exception exception) {
//            response.status(500);
//            return new Gson().toJson(new ErrorMessage(exception.getMessage()));
//        }
//    }
        return null;
    }
}
