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
    boolean hasDoubleMoved;
    boolean hasMoved;
    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.pieceColor = pieceColor;
        this.type = type;
        this.hasDoubleMoved = false;
        this.hasMoved = false;
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
            }
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
                    } else {
                        chessPiece = board.getPiece(row, column + i);
                        if (chessPiece != null) {
                            if (chessPiece.hasDoubleMoved && chessPiece.getTeamColor() != this.getTeamColor()) {
                                endPosition = new ChessPosition(row + direction, column + i);
                                availableMoves.add(new ChessMove(myPosition, endPosition,null));
                            }
                        }
                    }
                }
            }

        } else
        if (type==PieceType.ROOK) {
            rookMoveUp(row, column, board, myPosition, availableMoves);
            rookMoveRight(row, column, board, myPosition, availableMoves);
            rookMoveDown(row, column, board, myPosition, availableMoves);
            rookMoveLeft(row, column, board, myPosition, availableMoves);
        } else
        if (type==PieceType.KNIGHT) {
            int[][] listOfCoordinants = {{2,1},{1,2},{-1,2},{-2,1},{-2,-1},{-1,-2},{1,-2},{2,-1}};
            for (int[] coordinants : listOfCoordinants) {
                int rowPos = coordinants[0]+row;
                int colPos = coordinants[1]+column;
                if (isValidCoordinant(rowPos,colPos)) {
                    chessPiece = board.getPiece(rowPos,colPos);
                    if (chessPiece == null) {
                        endPosition = new ChessPosition(rowPos, colPos);
                        availableMoves.add(new ChessMove(myPosition, endPosition, null));
                    } else if (chessPiece.getTeamColor() != this.getTeamColor()) {
                        endPosition = new ChessPosition(rowPos, colPos);
                        availableMoves.add(new ChessMove(myPosition, endPosition, null));
                    }
                }
            }
        } else
        if (type==PieceType.BISHOP) {
            bishopMoveUpRight(row, column, board, myPosition, availableMoves);
            bishopMoveDownRight(row, column, board, myPosition, availableMoves);
            bishopMoveDownLeft(row, column, board, myPosition, availableMoves);
            bishopMoveUpLeft(row, column, board, myPosition, availableMoves);
        } else
        if (type==PieceType.QUEEN) {
            rookMoveUp(row, column, board, myPosition, availableMoves);
            rookMoveRight(row, column, board, myPosition, availableMoves);
            rookMoveDown(row, column, board, myPosition, availableMoves);
            rookMoveLeft(row, column, board, myPosition, availableMoves);
            bishopMoveUpRight(row, column, board, myPosition, availableMoves);
            bishopMoveDownRight(row, column, board, myPosition, availableMoves);
            bishopMoveDownLeft(row, column, board, myPosition, availableMoves);
            bishopMoveUpLeft(row, column, board, myPosition, availableMoves);
        } else
        if (type==PieceType.KING) {
            for (int i = -1; i<2; i++) {
                for (int j = -1; j<2; j++) {
                    if (!isValidCoordinant(row+i,column+j)) {continue;}
                    if (i==0 && j==0) {continue;}
                    chessPiece = board.getPiece(row+i,column+j);
                    endPosition = new ChessPosition(row+i,column+j);
                    if (chessPiece == null) {
                        availableMoves.add(new ChessMove(myPosition,endPosition,null));
                    } else if (chessPiece.getTeamColor() != this.getTeamColor()) {
                        availableMoves.add(new ChessMove(myPosition,endPosition,null));
                    }
                }
            }
            if (this.hasMoved == false) {
                castleOnRight(row, column, myPosition, board, availableMoves);
                castleOnLeft(row, column, myPosition, board, availableMoves);
            }
        }
        return availableMoves;
    }

    private void castleOnRight(int row, int column, ChessPosition myPosition, ChessBoard board, HashSet<ChessMove> availableMoves) {
        if (isValidCoordinant(row,column) && isValidCoordinant(row,column+1) && isValidCoordinant(row,column+2) && isValidCoordinant(row,column+3)) {
            ChessPosition endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn()+1);
            ChessPiece chessPiece = board.getPiece(endPosition);
            if (chessPiece == null) {
                endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn()+2);
                chessPiece = board.getPiece(endPosition);
                if (chessPiece == null) {
                    endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn()+3);
                    chessPiece = board.getPiece(endPosition);
                    if (chessPiece != null) {
                        if (chessPiece.getPieceType() == PieceType.ROOK && chessPiece.hasMoved == false) {
                            endPosition = new ChessPosition(myPosition.getRow(),myPosition.getColumn()+2);
                            availableMoves.add(new ChessMove(myPosition,endPosition,null));
                        }
                    }
                }
            }
        }
    }
    private void setPostionAndGetPiece(ChessPosition endPosition, ChessPosition myPosition, ChessPiece chessPiece, ChessBoard board, int distance) {
        endPosition = new ChessPosition(myPosition.getRow(),myPosition.getColumn()+distance);
        chessPiece = board.getPiece(endPosition);
    }
    private void castleOnLeft(int row, int column, ChessPosition myPosition, ChessBoard board, HashSet<ChessMove> availableMoves) {
        if (isValidCoordinant(row,column) && isValidCoordinant(row,column-1) && isValidCoordinant(row,column-2) && isValidCoordinant(row,column-3) && isValidCoordinant(row,column-4)) {
            ChessPosition endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn()-1);
            ChessPiece chessPiece = board.getPiece(endPosition);
            if (chessPiece == null) {
                endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn()-2);
                chessPiece = board.getPiece(endPosition);
                if (chessPiece == null) {
                    endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn()-3);
                    chessPiece = board.getPiece(endPosition);
                    if (chessPiece == null) {
                        endPosition = new ChessPosition(myPosition.getRow(), myPosition.getColumn()-4);
                        chessPiece = board.getPiece(endPosition);
                        if (chessPiece != null) {
                            if (chessPiece.getPieceType() == PieceType.ROOK && chessPiece.hasMoved == false) {
                                endPosition = new ChessPosition(myPosition.getRow(),myPosition.getColumn()-2);
                                availableMoves.add(new ChessMove(myPosition,endPosition,null));
                            }
                        }
                    }
                }
            }
        }
    }


    private void rookMoveUp(int row, int column, ChessBoard board, ChessPosition myPosition, HashSet<ChessMove> availableMoves) {
        ChessPiece chessPiece;
        ChessPosition endPosition;
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
    }
    private void rookMoveRight(int row, int column, ChessBoard board, ChessPosition myPosition, HashSet<ChessMove> availableMoves) {
        ChessPiece chessPiece;
        ChessPosition endPosition;
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
    }
    private void rookMoveDown(int row, int column, ChessBoard board, ChessPosition myPosition, HashSet<ChessMove> availableMoves) {
        ChessPiece chessPiece;
        ChessPosition endPosition;
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
    }
    private void rookMoveLeft(int row, int column, ChessBoard board, ChessPosition myPosition, HashSet<ChessMove> availableMoves) {
        ChessPiece chessPiece;
        ChessPosition endPosition;
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
    }
    private void bishopMoveUpRight(int row, int column, ChessBoard board, ChessPosition myPosition, HashSet<ChessMove> availableMoves) {
        ChessPiece chessPiece;
        ChessPosition endPosition;
        for (int i = 1;i<7;i++) {
            if (!isValidCoordinant(row+i,column+i)) {break;}
            chessPiece = board.getPiece(row+i,column+i);
            endPosition = new ChessPosition(row+i,column+i);
            if (chessPiece == null) {
                availableMoves.add(new ChessMove(myPosition,endPosition,null));
            } else if (chessPiece.getTeamColor() != this.getTeamColor()) {
                availableMoves.add(new ChessMove(myPosition,endPosition,null));
                break;
            } else {break;}
        }
    }
    private void bishopMoveDownRight(int row, int column, ChessBoard board, ChessPosition myPosition, HashSet<ChessMove> availableMoves) {
        ChessPiece chessPiece;
        ChessPosition endPosition;
        for (int i = 1;i<7;i++) {
            if (!isValidCoordinant(row-i,column+i)) {break;}
            chessPiece = board.getPiece(row-i,column+i);
            endPosition = new ChessPosition(row-i,column+i);
            if (chessPiece == null) {
                availableMoves.add(new ChessMove(myPosition,endPosition,null));
            } else if (chessPiece.getTeamColor() != this.getTeamColor()) {
                availableMoves.add(new ChessMove(myPosition,endPosition,null));
                break;
            } else {break;}
        }
    }
    private void bishopMoveDownLeft(int row, int column, ChessBoard board, ChessPosition myPosition, HashSet<ChessMove> availableMoves) {
        ChessPiece chessPiece;
        ChessPosition endPosition;
        for (int i = 1;i<7;i++) {
            if (!isValidCoordinant(row-i,column-i)) {break;}
            chessPiece = board.getPiece(row-i,column-i);
            endPosition = new ChessPosition(row-i,column-i);
            if (chessPiece == null) {
                availableMoves.add(new ChessMove(myPosition,endPosition,null));
            } else if (chessPiece.getTeamColor() != this.getTeamColor()) {
                availableMoves.add(new ChessMove(myPosition,endPosition,null));
                break;
            } else {break;}
        }
    }
    private void bishopMoveUpLeft(int row, int column, ChessBoard board, ChessPosition myPosition, HashSet<ChessMove> availableMoves) {
        ChessPiece chessPiece;
        ChessPosition endPosition;
        for (int i = 1;i<7;i++) {
            if (!isValidCoordinant(row+i,column-i)) {break;}
            chessPiece = board.getPiece(row+i,column-i);
            endPosition = new ChessPosition(row+i,column-i);
            if (chessPiece == null) {
                availableMoves.add(new ChessMove(myPosition,endPosition,null));
            } else if (chessPiece.getTeamColor() != this.getTeamColor()) {
                availableMoves.add(new ChessMove(myPosition,endPosition,null));
                break;
            } else {break;}
        }
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
