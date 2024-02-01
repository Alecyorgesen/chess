package chess;

import java.util.Collection;
import java.util.HashSet;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    TeamColor teamTurn;
    ChessBoard board = new ChessBoard();
    public ChessGame() {
        teamTurn = TeamColor.WHITE;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = board.getPiece(startPosition);
        HashSet<ChessMove> validMoves = new HashSet<>();
        if (piece != null) {
            HashSet<ChessMove> availableMoves = (HashSet<ChessMove>) piece.pieceMoves(board,startPosition);
            for (ChessMove move : availableMoves) {
                ChessBoard boardCopy = this.boardClone();
                ChessPiece pieceCopy = boardCopy.getPiece(move.getStartPosition());
                this.forceMove(boardCopy,pieceCopy,move);
                if (!isInCheck(piece.getTeamColor(),boardCopy)) {
                    validMoves.add(move);
                }
            }
            if (validMoves.isEmpty()) {
                return null;
            } else {
                return validMoves;
            }
        }
        return null;
    }
    private void forceMove(ChessBoard board, ChessPiece piece, ChessMove move) {
        ChessPiece.PieceType type = move.getPromotionPiece();
        if (type != null) {
            ChessPiece promotedPiece = new ChessPiece(piece.getTeamColor(), type);
            board.setPiece(move.getEndPosition(), promotedPiece);
        } else {
            board.setPiece(move.getEndPosition(), piece);
        }
        board.removePiece(move.getStartPosition());
    }
    private ChessBoard boardClone() {
        ChessBoard boardClone;
        try {
            boardClone = (ChessBoard) board.clone();
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException("Board didn't clone correctly in validMoves method");
        }
        return boardClone;
    }
    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece piece = board.getPiece(move.getStartPosition());
        if (piece != null) {
            if (piece.getTeamColor() == teamTurn) {
                for (ChessMove possibleMove : this.validMoves(move.getStartPosition())) {
                    if (possibleMove.equals(move)) {
                        this.forceMove(this.board,piece,move);
                        switchTurn();
                        return;
                    }
                }
            }
        }
        throw new InvalidMoveException("Move is invalid.");
    }
    private void switchTurn() {
        if (this.teamTurn == TeamColor.WHITE) {
            this.teamTurn = TeamColor.BLACK;
        } else {
            this.teamTurn = TeamColor.WHITE;
        }
    }
    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition position = new ChessPosition(i,j);
                ChessPiece piece = board.getPiece(position);
                ChessPiece targetPiece;
                if (piece != null) {
                    if (piece.getTeamColor() != teamColor) {
                        for (ChessMove move : piece.pieceMoves(board,position)) {
                            targetPiece = board.getPiece(move.getEndPosition());
                            if (targetPiece != null) {
                                if (targetPiece.getPieceType() == ChessPiece.PieceType.KING) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
    public boolean isInCheck(TeamColor teamColor, ChessBoard board) {
        for (int i = 1; i < 9; i++) {
            for (int j = 1; j < 9; j++) {
                ChessPosition position = new ChessPosition(i,j);
                ChessPiece piece = board.getPiece(position);
                ChessPiece targetPiece;
                if (piece != null) {
                    if (piece.getTeamColor() != teamColor) {
                        for (ChessMove move : piece.pieceMoves(board,position)) {
                            targetPiece = board.getPiece(move.getEndPosition());
                            if (targetPiece != null) {
                                if (targetPiece.getPieceType() == ChessPiece.PieceType.KING) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (isInCheck(teamColor)) {
            for (int i=1;i<9;i++) {
                for (int j=1;j<9;j++) {
                    ChessPosition position = new ChessPosition(i,j);
                    ChessPiece piece = board.getPiece(position);
                    if (piece != null) {
                        if (piece.getTeamColor() == teamColor) {
                            if (this.validMoves(position) != null) {
                                return false;
                            }
                        }
                    }
                }
            }
            return true;
        }
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        for (int i=1;i<9;i++) {
            for (int j=1;j<9;j++) {
                ChessPosition position = new ChessPosition(i,j);
                ChessPiece piece = board.getPiece(position);
                if (piece != null) {
                    if (piece.getTeamColor() == teamColor) {
                        if (this.validMoves(position) != null) {
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }
}
