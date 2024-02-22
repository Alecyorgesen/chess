package model;

import chess.ChessGame;

record GameData() {
    static int gameId;
    static String whiteUsername;
    static String blackUsername;
    static String gameName;
    static ChessGame game;
}
