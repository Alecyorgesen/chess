package handler;

import com.google.gson.Gson;
import error.BadRequestException;
import error.UnauthorizedException;
import message.ErrorMessage;
import message.CreateGameResponse;
import message.CreateGameRequest;
import service.CreateGameService;
import spark.Request;
import spark.Response;

public class CreateGameHandler {
    public static final CreateGameService createGameService = new CreateGameService();
    public Object createGame(Request request, Response response) {
        try {
            CreateGameRequest gameName = new Gson().fromJson(request.body(), CreateGameRequest.class);
            int gameId = createGameService.createGame(request.headers("Authorization"), gameName.gameName());
            response.status(200);
            return new Gson().toJson(new CreateGameResponse(gameId));
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
