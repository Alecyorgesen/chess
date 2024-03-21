package ui;

import chess.ChessBoard;
import chess.ChessPiece;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ChessBoardPrinter {
    public void printChessBoard(ChessBoard board) {
        final int SQUARE_LENGTH = 3;
        final int CHESS_BOARD_WIDTH = 10 * SQUARE_LENGTH;
        final String BLANK_SQUARE = "   ";

        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(EscapeSequences.ERASE_SCREEN);
        out.print(EscapeSequences.SET_BG_COLOR_BLACK);
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

                    case ROOK:

                    case KNIGHT:

                    case BISHOP:

                    case QUEEN:

                    case KING:
                }
            }
        }


        out.print(BLANK_SQUARE);

        
    }
}
