package handler;

import com.google.gson.Gson;
import error.BadRequestException;
import error.UnauthorizedException;
import message.ErrorMessage;
import model.GameData;
import service.CreateGameService;
import spark.Request;
import spark.Response;

public class CreateGameHandler {
    public static final CreateGameService createGameService = new CreateGameService();
    public Object createGame(Request request, Response response) {
        try {
            String gameName = new Gson().toJson(request.body(), String.class);
            int gameId = createGameService.createGame(request.headers("Authorization"), gameName);
            response.status(200);
            return new Gson().toJson(gameId);
        } catch (BadRequestException exception) {
            response.status(400);
            return new Gson().toJson(new ErrorMessage(exception.getMessage()));
        } catch (UnauthorizedException exception) {
            response.status(401);
            return new Gson().toJson(new ErrorMessage(exception.getMessage()));
        } catch (Exception exception) {
            response.status(500);
            return new Gson().toJson(new ErrorMessage(exception.getMessage()));
        }
    }
}
