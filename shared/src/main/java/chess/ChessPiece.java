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
        if (type==PieceType.PAWN) {
            if (this.pieceColor == ChessGame.TeamColor.BLACK) {direction = -1;} //This line tells it to go the opposite way if black.
            if (board.getPiece(row + direction, column) == null) {
                endPosition = new ChessPosition(row + direction, column);
                if (row + direction == 1 || row + direction == 8) {
                    PieceType[] promotionTypeArray = {PieceType.QUEEN,PieceType.ROOK,PieceType.BISHOP,PieceType.KNIGHT};
                    for (PieceType type : promotionTypeArray) {
                        availableMoves.add(new ChessMove(myPosition,endPosition,type));
                    }
                } else {
                    availableMoves.add(new ChessMove(myPosition,endPosition,null));
                }
                if (row == 2 || row == 7) {
                    if (isValidCoordinant(row + direction*2, column)) {
                        if (board.getPiece(row + direction*2, column) == null) {
                            endPosition = new ChessPosition(row + direction*2, column);
                            availableMoves.add(new ChessMove(myPosition,endPosition,null));
                        }
                    }
                }
            } // hey, u r cool btw
            int[] attackPosititons = {1, -1};
            for (int i : attackPosititons) {
                if (isValidCoordinant(row + direction, column + i)) {
                    chessPiece = board.getPiece(row + direction, column + i);
                    if (chessPiece != null) {
                        if (chessPiece.getTeamColor() != this.getTeamColor()) {
                            endPosition = new ChessPosition(row + direction, column + i);
                            if (row + direction == 1 || row + direction == 8) {
                                PieceType[] promotionTypeArray = {PieceType.QUEEN,PieceType.ROOK,PieceType.BISHOP,PieceType.KNIGHT};
                                for (PieceType type : promotionTypeArray) {
                                    availableMoves.add(new ChessMove(myPosition,endPosition,type));
                                }
                            } else {
                                availableMoves.add(new ChessMove(myPosition,endPosition,null));
                            }
                        }
                    }
                }
            }

        } else if (type==PieceType.ROOK) {
            for (int i = row+1;i<9;i++) {
                if (isValidCoordinant(i,column)) {
                    chessPiece = board.getPiece(i,column);
                    endPosition = new ChessPosition(i, column);
                    if (chessPiece == null) {
                        availableMoves.add(new ChessMove(myPosition, endPosition, null));
                    } else if (chessPiece.getTeamColor() != this.getTeamColor()) {
                        availableMoves.add(new ChessMove(myPosition, endPosition, null));
                        break;
                    } else {
                        break;
                    }
                } else {break;}
            }
            for (int i = row-1;i>0;i--) {
                if (isValidCoordinant(i,column)) {
                    chessPiece = board.getPiece(i,column);
                    endPosition = new ChessPosition(i, column);
                    if (chessPiece == null) {
                        availableMoves.add(new ChessMove(myPosition, endPosition, null));
                    } else if (chessPiece.getTeamColor() != this.getTeamColor()) {
                        availableMoves.add(new ChessMove(myPosition, endPosition, null));
                        break;
                    } else {
                        break;
                    }
                } else {break;}
            }
            for (int i = column+1;i<9;i++) {
                if (isValidCoordinant(row,i)) {
                    chessPiece = board.getPiece(row,i);
                    endPosition = new ChessPosition(row,i);
                    if (chessPiece == null) {
                        availableMoves.add(new ChessMove(myPosition, endPosition, null));
                    } else if (chessPiece.getTeamColor() != this.getTeamColor()) {
                        availableMoves.add(new ChessMove(myPosition, endPosition, null));
                        break;
                    } else {
                        break;
                    }
                } else {break;}
            }
            for (int i = column-1;i>0;i--) {
                if (isValidCoordinant(row,i)) {
                    chessPiece = board.getPiece(row,i);
                    endPosition = new ChessPosition(row,i);
                    if (chessPiece == null) {
                        availableMoves.add(new ChessMove(myPosition, endPosition, null));
                    } else if (chessPiece.getTeamColor() != this.getTeamColor()) {
                        availableMoves.add(new ChessMove(myPosition, endPosition, null));
                        break;
                    } else {
                        break;
                    }
                } else {break;}
            }
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
