import chess.*;
import client.Client;

public class Main {
    public static Client client = new Client();
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Client: " + piece);
        client.run();
    }
}