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
            //use add/remove to apply move, then call isInCheck
//            gameCopy.setBoard(boardCopy);
//            try {
//            gameCopy.makeMove(move); }
//            catch (Exception InvalidMoveException) {
//                //what to do here?
//            }
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
        //check if move is valid
        //call valid moves on startposition of move, see if move is in returned collection
        //else return invalid move exception
        if (move.getPromotionPiece() != null) {
            chessboard.addPiece(move.getEndPosition(), new ChessPiece(chessboard.getPiece(move.getStartPosition()).getTeamColor(),move.getPromotionPiece()));
        } else {
            chessboard.addPiece(move.getEndPosition(),chessboard.getPiece(move.getStartPosition()));
            chessboard.removePiece(move.getStartPosition());
        }
        throw new InvalidMoveException("Illegal Move");
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        //find kingpos
        ChessPosition kingPos = null;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (chessboard.pieceFound(i,j) && chessboard.getPiece(new ChessPosition(i,j)).equals(new ChessPiece(teamColor, ChessPiece.PieceType.KING))) {
                    kingPos = new ChessPosition(i,j);
                }
            }
        }
        //opposing team has a piece that could capture the king
        //iterate through board, if piece at position !- teamColor, call piecemoves to get list of moves
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (chessboard.pieceFound(i,j) && chessboard.getPiece(new ChessPosition(i,j)).getTeamColor() != teamColor) {
                    Collection<ChessMove> enemyMoves = chessboard.getPiece(new ChessPosition(i,j)).pieceMoves(chessboard, new ChessPosition(i,j));
                    for (ChessMove move : enemyMoves) {
                        if (move.getEndPosition().equals(kingPos)) return true;
                    }
                }
                }
            }
        //iterate through moves list to see if there is move where endpos .equals kingpos
        //else return false
        return false;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        //is in check, validMoves returns null
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
