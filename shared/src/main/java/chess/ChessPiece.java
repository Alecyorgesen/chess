package chess;

import java.awt.*;
import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {
    ChessGame.TeamColor pieceColor;
    ChessPiece.PieceType type;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return this.pieceColor;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return this.type;
    }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        HashSet<ChessMove> availableMoves = new HashSet<ChessMove>();
        int row = myPosition.getRow();
        int column = myPosition.getColumn();
        int direction = 1;
        ChessPosition endPosition;
        ChessPiece chessPiece;
//        ChessPosition position = new ChessPosition(row, column);
        if (type==PieceType.PAWN) {
            if (this.pieceColor == ChessGame.TeamColor.BLACK) {direction = -1;}

            if (board.getPiece(row, column + direction) == null) {
                endPosition = new ChessPosition(row, column + direction);
                availableMoves.add(new ChessMove(myPosition,endPosition,null));
                if (row == 2 || row == 7) {
                    if (isValidCoordinant(row, column)) {
                        if (board.getPiece(row, column + direction*2) == null) {
                            endPosition = new ChessPosition(row, column + direction*2);
                            availableMoves.add(new ChessMove(myPosition,endPosition,null));
                        }
                    }
                }
            }
            int[] attackPosititons = {1, -1};
            for (int i : attackPosititons) {
                if (isValidCoordinant(row + i, column + direction)) {
                    chessPiece = board.getPiece(row + i, column + direction);
                    if (chessPiece != null) {
                        if (chessPiece.getTeamColor() != this.getTeamColor()) {
                            endPosition = new ChessPosition(row + i, column + direction);
                            availableMoves.add(new ChessMove(myPosition, endPosition, null));
                        }
                    }
                }
            }

        } else if (type==PieceType.ROOK) {

        } else if (type==PieceType.KNIGHT) {

        } else if (type==PieceType.BISHOP) {

        } else if (type==PieceType.QUEEN) {

        } else if (type==PieceType.KING) {

        }
        return availableMoves; //something
    }

    private boolean isValidCoordinant(int row, int column) {
        if (row < 1 || row > 8) {
            return false;
        } else if (column < 1 || column > 8) {
            return false;
        } else {
            return true;
        }
    }
    @Override
    public String toString() {
        return "ChessPiece{" +
                "pieceColor=" + pieceColor +
                ", type=" + type +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessPiece that = (ChessPiece) o;
        return pieceColor == that.pieceColor && type == that.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(pieceColor, type);
    }
}
