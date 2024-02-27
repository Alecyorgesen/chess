package handler;

import com.google.gson.Gson;
import error.AlreadyTakenException;
import error.BadRequestException;
import error.UnauthorizedException;
import message.JoinGameRequest;
import message.ErrorMessage;
import service.JoinGameService;
import spark.Request;
import spark.Response;

public class JoinGameHandler {
    JoinGameService joinGameService = new JoinGameService();

    public Object joinGame(Request request, Response response) {
        try {
            JoinGameRequest colorAndGameID = new Gson().fromJson(request.body(), JoinGameRequest.class);
            joinGameService.joinGame(request.headers("Authorization"), colorAndGameID);
            response.status(200);
            return "{}";
        } catch (BadRequestException exception) {
            response.status(400);
            return new Gson().toJson(new ErrorMessage(exception.getMessage()));
        } catch (UnauthorizedException exception) {
            response.status(401);
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
