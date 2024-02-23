package dataAccess;

import java.util.*;
import chess.*;
public class GameDataModel {
    static List<String> values;
    public GameDataModel() {
        GameDataModel.values = new ArrayList<>();
    }

    public List<String> getGames() {
        return GameDataModel.values;
    }

    public void addValues(ChessGame game) {
        GameDataModel.values.add(game);
    }
}
