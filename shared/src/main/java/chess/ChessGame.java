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
    private ChessBoard chessboard=new ChessBoard();
    private TeamColor teamTurn=TeamColor.WHITE;

    public ChessGame() {
        chessboard.resetBoard();
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
        teamTurn=team;
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
        //return null if no piece found
        if (!chessboard.pieceFound(new ChessPosition(startPosition.getRow(), startPosition.getColumn()))) {
            return null;
        }
        Collection<ChessMove> validMoves=new ArrayList<>();
        Collection<ChessMove> moves=chessboard.getPiece(startPosition).pieceMoves(chessboard, startPosition);
        for (ChessMove move : moves) {
            ChessBoard boardCopy=new ChessBoard(chessboard);
            boardCopy.movePiece(move, boardCopy.getPiece(move.getStartPosition()));
            //use add/remove to apply move,
            if (!isInCheck(chessboard.getPiece(startPosition).getTeamColor(), boardCopy)) {
                validMoves.add(move);
            }
            //iterate through collection, creating copy of board with each iteration
            //play suggested move on board copy, call isInCheck --if true, add to collection validMoves
            //else don't add, iterate to next
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
        //check move is valid
        //call valid moves on startposition of move, see if move is in returned collection
        //else return invalid move exception
        Collection<ChessMove> validMoves=validMoves(move.getStartPosition());
        if (chessboard.pieceFound(move.getStartPosition()) &&
                chessboard.getPiece(move.getStartPosition()).getTeamColor() == teamTurn &&
                validMoves.contains(move)) {
            chessboard.movePiece(move, chessboard.getPiece(move.getStartPosition()));
            if (teamTurn == TeamColor.BLACK) {
                teamTurn=TeamColor.WHITE;
            } else {
                teamTurn=TeamColor.BLACK;
            }
        } else {
            throw new InvalidMoveException("Illegal Move");
        }
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        return isInCheck(teamColor, chessboard);
    }

    private boolean isInCheck(TeamColor teamColor, ChessBoard board) {
        //find kingpos
        ChessPosition kingPos=null;
        for (int i=1; i < 9; i++) {
            for (int j=1; j < 9; j++) {
                if (board.pieceFound(new ChessPosition(i, j)) &&
                        board.getPiece(new ChessPosition(i, j)).equals(new ChessPiece(teamColor,
                                ChessPiece.PieceType.KING))) {
                    kingPos=new ChessPosition(i, j);
                }
            }
        }
        //opposing team has a piece that could capture the king
        //iterate through board, if piece at position !- teamColor, call piecemoves to get list of moves
        for (int i=1; i < 9; i++) {
            for (int j=1; j < 9; j++) {
                if (board.pieceFound(new ChessPosition(i, j)) && board.getPiece(new ChessPosition(i, j)).getTeamColor() != teamColor) {
                    Collection<ChessMove> enemyMoves=board.getPiece(new ChessPosition(i, j)).pieceMoves(board, new ChessPosition(i, j));
                    if (isAttackingKing(enemyMoves, kingPos)) {
                        return true;
                    }
                }
            }
        }
        //iterate through moves list to see if there is move where endpos .equals kingpos
        //else return false
        return false;
    }

    private boolean isAttackingKing(Collection<ChessMove> enemyMoves, ChessPosition kingPos) {
        for (ChessMove move : enemyMoves) {
            if (move.getEndPosition().equals(kingPos)) {
                return true;
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
        //is in check, validMoves returns null
        //iterate through board, find moves that teamColor could do
        //if empty, and isInCheck is true, then return true
        boolean hasMoves=teamHasMoves(teamColor);
        return isInCheck(teamColor) && !hasMoves;
    }

    private boolean teamHasMoves(TeamColor teamColor) {
        boolean hasMoves=false;
        Collection<ChessMove> moves=new ArrayList<>();
        for (int i=1; i < 9; i++) {
            for (int j=1; j < 9; j++) {
                if (chessboard.pieceFound(new ChessPosition(i, j)) && chessboard.getPiece(new ChessPosition(i, j)).getTeamColor() == teamColor) {
                    moves=validMoves(new ChessPosition(i, j));
                    if (!moves.isEmpty()) {
                        hasMoves=true;
                        break;
                    }
                }
            }
        }
        return hasMoves;
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
        boolean hasMoves=teamHasMoves(teamColor);
        return !isInCheck(teamColor) && !hasMoves;
        //if not in check and validMoves returns empty
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        chessboard=board;
        //also add all pieces to collection to track them

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
