package service;

import dataAccess.AuthDAO;
import dataAccess.AuthMemoryDAO;
import dataAccess.GameDAO;
import dataAccess.GameMemoryDAO;
import model.GameData;

import java.util.List;

public class ListGamesService {
    public static final GameDAO gameDAO = new GameMemoryDAO();
    public static final AuthDAO authDAO = new AuthMemoryDAO();
    public ListGamesService(){

    }

    public GameData listGames(String authToken){
//        if ()
    }
}
