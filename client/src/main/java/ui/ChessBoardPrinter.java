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
        board.resetBoard();

        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(EscapeSequences.ERASE_SCREEN);

        printTopSideAlsoBottomSide(out);
        printMiddlePartOfBoard(out, board);
        printTopSideAlsoBottomSide(out);

        out.print(BLANK_SQUARE);

        
    }

    void printTopSideAlsoBottomSide(PrintStream out) {
        out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
        out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
        out.print(BLANK_SQUARE);
        String[] letterCoordinates = {" a ", " b ", " c ", " d ", " e ", " f ", " g ", " h "};
        for (String letter : letterCoordinates) {
            out.print(letter);
        }
        out.print(BLANK_SQUARE);
        out.println();
    }

    void printMiddlePartOfBoard(PrintStream out, ChessBoard board) {
        ChessPiece[][] chessPieceMatrix = getPiecesAsMatrix(board);
        String[] numberCoordinates = {" 1 ", " 2 ", " 3 ", " 4 ", " 5 ", " 6 ", " 7 ", " 8 "};
        for (int i = 1; i < 9; i++) {
            out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
            out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
            out.print(numberCoordinates[i-1]);
            for (int j = 1; j < 9; j++) {
                if (!isSumEven(i,j)) {
                    out.print(EscapeSequences.SET_BG_COLOR_BLACK);
                } else {
                    out.print(EscapeSequences.SET_BG_COLOR_WHITE);
                }
                if (chessPieceMatrix[i][j] != null) {
                    if (chessPieceMatrix[i][j].getTeamColor() == ChessGame.TeamColor.WHITE) {
                        out.print(EscapeSequences.SET_TEXT_COLOR_RED);
                    } else {
                        out.print(EscapeSequences.SET_TEXT_COLOR_MAGENTA);
                    }
                    printLetterBasedOnChessPiece(out, i, j, chessPieceMatrix);
                } else {
                    out.print(BLANK_SQUARE);
                }
            }
            out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
            out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
            out.print(numberCoordinates[i-1]);
            out.println();
        }
    }
    boolean isSumEven(int num1, int num2) {
        int sum = num1 + num2;
        int remainder = sum % 2;
        return remainder == 0;
    }
    void printLetterBasedOnChessPiece(PrintStream out, int i, int j, ChessPiece[][] chessPiecesMatrix) {
        ChessPiece chessPiece = chessPiecesMatrix[i][j];
        switch (chessPiece.getPieceType()) {
            case PAWN:
                if (chessPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                    out.print(" P ");
                } else {
                    out.print(" p ");
                }
                return;
            case ROOK:
                if (chessPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                    out.print(" R ");
                } else {
                    out.print(" r ");
                }
                return;
            case KNIGHT:
                if (chessPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                    out.print(" N ");
                } else {
                    out.print(" n ");
                }
                return;
            case BISHOP:
                if (chessPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                    out.print(" B ");
                } else {
                    out.print(" b ");
                }
                return;
            case QUEEN:
                if (chessPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                    out.print(" Q ");
                } else {
                    out.print(" q ");
                }
                return;
            case KING:
                if (chessPiece.getTeamColor() == ChessGame.TeamColor.WHITE) {
                    out.print(" K ");
                } else {
                    out.print(" k ");
                }
            }
    }
    ChessPiece[][] getPiecesAsMatrix(ChessBoard board) {
        ChessPiece[][] chessPiecesMatrix = new ChessPiece[9][9];
        for (int i = 1; i < 9; i++) {
            for (int j = 8; j >= 1; j--) {
                chessPiecesMatrix[i][j] = board.getPiece(i, j);
            }
        }
        return chessPiecesMatrix;
    }
}
