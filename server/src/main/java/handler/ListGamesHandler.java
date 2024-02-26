package handler;

import error.UnauthorizedException;
import model.GameData;
import service.ListGamesService;
import spark.Request;
import spark.Response;

public class ListGamesHandler {
    public static final ListGamesService listGamesService = new ListGamesService();
    public ListGamesHandler(){

    }

    public GameData listGames(Request request, Response response) {
        try {
            listGamesService.listGames(request.headers("Authorization"));
        } catch (UnauthorizedException exception) {

        } catch (Exception exception) {

        }
    }
}
