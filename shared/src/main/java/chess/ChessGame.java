package chess;

import java.util.ArrayList;
import java.util.Collection;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private ChessBoard chessboard;
    TeamColor teamTurn;
    //Collection<ChessPiece> whitePieces;
    //Collection<ChessPiece> blackPieces;
    public ChessGame() {

    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        teamTurn = team;
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
        //call pieceMoves on board.getPiece(startPosition)
        Collection<ChessMove> validMoves = new ArrayList<>();
        Collection<ChessMove> moves = chessboard.getPiece(startPosition).pieceMoves(chessboard, startPosition);
        for (ChessMove move : moves) {
            ChessBoard boardCopy = new ChessBoard(chessboard);
            ChessGame gameCopy = new ChessGame();
            gameCopy.setBoard(boardCopy);
            gameCopy.makeMove(move);
            if (!gameCopy.isInCheck(teamTurn)) {
                validMoves.add(move);
            }
            //iterate through collection, creating copy of board with each iteration
            //play suggested move on board copy, call isInCheck --if true, add to collection validMoves
            //else don't add, iterate to next
            //returns null if collection is empty
        }
        return validMoves;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        //apply move to board, removing piece if captured
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        //opposing team has a piece that could capture the king, but validMoves returns collection
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        //is in check, team has no valid moves to make
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        //check for valid moves
//        Collection<ChessMove> moves;
//        if (teamColor == TeamColor.WHITE) {
//            for (ChessPiece piece : whitePieces) {
//                 moves.addAll(validMoves(piece.))
//            }
//        }
        //return (getTeamTurn() == teamColor && !isInCheck(teamColor))

        //if not in check and validMoves returns empty
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        chessboard = board;
        //also add all pieces to collection to track them
//        for (int i = 0; i < 8; i++) {
//            whitePieces.add(chessboard.getPiece(new ChessPosition(0,i)));
//            whitePieces.add(chessboard.getPiece(new ChessPosition(1,i)));
//            blackPieces.add(chessboard.getPiece(new ChessPosition(6,i)));
//            blackPieces.add(chessboard.getPiece(new ChessPosition(7,i)));
//        }
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return chessboard;
    }
}
