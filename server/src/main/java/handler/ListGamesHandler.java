package handler;

import com.google.gson.Gson;
import error.UnauthorizedException;
import message.ErrorMessage;
import message.ListGamesResponse;
import service.ListGamesService;
import spark.Request;
import spark.Response;

public class ListGamesHandler {
    public static final ListGamesService listGamesService = new ListGamesService();
    public ListGamesHandler(){

    }

    public Object listGames(Request request, Response response) {
        try {
            ListGamesResponse games = listGamesService.listGames(request.headers("Authorization"));
            response.status(200);
            return new Gson().toJson(games);
        } catch (UnauthorizedException exception) {
            response.status(401);
            return new Gson().toJson(new ErrorMessage(exception.getMessage()));
        } catch (Exception exception) {
            response.status(500);
            return new Gson().toJson(new ErrorMessage(exception.getMessage()));
        }
    }
}
