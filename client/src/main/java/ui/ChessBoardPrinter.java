package ui;

import chess.ChessBoard;
import chess.ChessGame;
import chess.ChessPiece;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ChessBoardPrinter {
    final int SQUARE_LENGTH = 3;
    final int CHESS_BOARD_WIDTH = 10 * SQUARE_LENGTH;
    final String BLANK_SQUARE = "   ";
    public void printChessBoard(ChessBoard board) {

        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(EscapeSequences.ERASE_SCREEN);
        out.print(EscapeSequences.SET_BG_COLOR_BLACK);

        String[][] stringMatrix = getPiecePositionsAsStrings(board);

        out.print(BLANK_SQUARE);

        
    }

    String[][] getPiecePositionsAsStrings(ChessBoard board) {
        String[][] stringMatrix = new String[9][9];
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPiece chessPiece = board.getPiece(i, j);
                if (chessPiece == null) {
                    stringMatrix[i][j] = BLANK_SQUARE;
                    continue;
                }
                switch (chessPiece.getPieceType()) {
                    case PAWN:
                        if (chessPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                            stringMatrix[i][j] = " P ";
                        } else {
                            stringMatrix[i][j] = " p ";
                        }
                        continue;
                    case ROOK:
                        if (chessPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                            stringMatrix[i][j] = " R ";
                        } else {
                            stringMatrix[i][j] = " r ";
                        }
                        continue;
                    case KNIGHT:
                        if (chessPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                            stringMatrix[i][j] = " N ";
                        } else {
                            stringMatrix[i][j] = " n ";
                        }
                        continue;
                    case BISHOP:
                        if (chessPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                            stringMatrix[i][j] = " B ";
                        } else {
                            stringMatrix[i][j] = " b ";
                        }
                        continue;
                    case QUEEN:
                        if (chessPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                            stringMatrix[i][j] = " Q ";
                        } else {
                            stringMatrix[i][j] = " q ";
                        }
                        continue;
                    case KING:
                        if (chessPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                            stringMatrix[i][j] = " K ";
                        } else {
                            stringMatrix[i][j] = " k ";
                        }
                }
            }
        }
        return stringMatrix;
    }
}
