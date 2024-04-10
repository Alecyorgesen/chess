package ui;

import chess.*;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

public class ChessBoardPrinter {
    final int SQUARE_LENGTH = 3;
    final int CHESS_BOARD_WIDTH = 10 * SQUARE_LENGTH;
    final String BLANK_SQUARE = "   ";
    public void printBoardFromWhiteSide(ChessBoard board) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(EscapeSequences.ERASE_SCREEN);
        printBoardFromWhitePerspective(out, board, null, null);
    }
    public void printBoardFromBlackSide(ChessBoard board) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(EscapeSequences.ERASE_SCREEN);
        printBoardFromBlackPerspective(out, board, null, null);
    }
    public void printChessBoard(ChessBoard board) {
        board.resetBoard();

        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

        out.print(EscapeSequences.ERASE_SCREEN);

        printBoardFromBlackPerspective(out, board, null, null);
        printSpaceInBetweenBoards(out);
        printBoardFromWhitePerspective(out, board, null, null);

//        out.print(BLANK_SQUARE);
    }
    public void printBoardFromWhiteSideWithHighlight(ChessBoard board, ChessGame chessGame, ChessPosition chessPosition) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(EscapeSequences.ERASE_SCREEN);
        printBoardFromWhitePerspective(out, board, chessGame, chessPosition);
    }
    public void printBoardFromBlackSideWithHighlight(ChessBoard board, ChessGame chessGame, ChessPosition chessPosition) {
        var out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        out.print(EscapeSequences.ERASE_SCREEN);
        printBoardFromBlackPerspective(out, board, chessGame, chessPosition);
    }
    void printSpaceInBetweenBoards(PrintStream out) {
        out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
        out.print(BLANK_SQUARE.repeat(9));
        out.println();
    }
    void printBoardFromWhitePerspective(PrintStream out, ChessBoard board, ChessGame chessGame, ChessPosition chessPosition) {
        printTopSideAlsoBottomSide(out, true);
        printMiddlePartOfBoard(out, board, true, chessGame, chessPosition);
        printTopSideAlsoBottomSide(out, true);
    }
    void printBoardFromBlackPerspective(PrintStream out, ChessBoard board, ChessGame chessGame, ChessPosition chessPosition) {
        printTopSideAlsoBottomSide(out, false);
        printMiddlePartOfBoard(out, board, false, chessGame, chessPosition);
        printTopSideAlsoBottomSide(out, false);
    }

    void printTopSideAlsoBottomSide(PrintStream out, Boolean whitePerspective) {
        out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
        out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
        out.print(BLANK_SQUARE);
        String[] letterCoordinates;
        if (whitePerspective) {
            letterCoordinates = new String[]{" a ", " b ", " c ", " d ", " e ", " f ", " g ", " h "};
        } else {
            letterCoordinates = new String[]{" h ", " g ", " f ", " e ", " d ", " c ", " b ", " a "};
        }
        for (String letter : letterCoordinates) {
            out.print(letter);
        }
        out.print(BLANK_SQUARE);
        out.println();
    }

    void printMiddlePartOfBoard(PrintStream out, ChessBoard board, Boolean whitePerspective, ChessGame chessGame, ChessPosition chessPosition) {
        ChessPiece[][] chessPieceMatrix = getPiecesAsMatrix(board, whitePerspective);
        String[] numberCoordinates;
        if (whitePerspective) {
            numberCoordinates = new String[]{" 8 ", " 7 ", " 6 ", " 5 ", " 4 ", " 3 ", " 2 ", " 1 "};
        } else {
            numberCoordinates = new String[]{" 1 ", " 2 ", " 3 ", " 4 ", " 5 ", " 6 ", " 7 ", " 8 "};
        }
        for (int i = 1; i < 9; i++) {
            out.print(EscapeSequences.SET_BG_COLOR_DARK_GREY);
            out.print(EscapeSequences.SET_TEXT_COLOR_WHITE);
            out.print(numberCoordinates[i-1]);
            for (int j = 1; j < 9; j++) {
                boolean isPiecePosition = false;
                ChessPosition piecePosition = new ChessPosition(i, j);
                if (piecePosition.equals(chessPosition)) {
                    isPiecePosition = true;
                }
                boolean validSpot = false;
                if (chessGame != null) {
                    HashSet<ChessMove> possibleMoves = (HashSet<ChessMove>) chessGame.validMoves(chessPosition);
                    ChessPosition currentPosition = new ChessPosition(i,j);
                    for (ChessMove move : possibleMoves) {
                        if (move.getEndPosition().equals(currentPosition)) {
                            validSpot = true;
                            break;
                        }
                    }
                }
                if (!isSumEven(i,j)) {
                    if (validSpot) {
                        out.print(EscapeSequences.SET_BG_COLOR_DARK_GREEN);
                    } else {
                        out.print(EscapeSequences.SET_BG_COLOR_BLACK);
                    }
                    if (isPiecePosition) {
                        out.print(EscapeSequences.SET_BG_COLOR_YELLOW);
                    }
                } else {
                    if (validSpot) {
                        out.print(EscapeSequences.SET_BG_COLOR_GREEN);
                    } else {
                        out.print(EscapeSequences.SET_BG_COLOR_WHITE);
                    }
                    if (isPiecePosition) {
                        out.print(EscapeSequences.SET_BG_COLOR_YELLOW);
                    }
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
    ChessPiece[][] getPiecesAsMatrix(ChessBoard board, Boolean whitePerspective) {
        ChessPiece[][] chessPiecesMatrix = new ChessPiece[9][9];
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                if (whitePerspective) {
                    chessPiecesMatrix[flipCoordinate(i)][j] = board.getPiece(i, j);
                } else {
                    chessPiecesMatrix[i][flipCoordinate(j)] = board.getPiece(i, j);
                }
            }
        }
        return chessPiecesMatrix;
    }
    private int flipCoordinate(int number) {
        return switch (number) {
            case 1 -> 8;
            case 2 -> 7;
            case 3 -> 6;
            case 4 -> 5;
            case 5 -> 4;
            case 6 -> 3;
            case 7 -> 2;
            case 8 -> 1;
            default -> number;
        };
    }
}
