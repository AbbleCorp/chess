package chess;
/**
 * A calculator that returns an array list of possible moves according to the
 * type of the piece. Subclasses for each piece type.
 *
 * Static Nested Classes??
 */
public class PieceMoveCalc {
    private ChessPiece.PieceType type;
    private ChessGame.TeamColor color;

    PieceMoveCalc(ChessPiece.PieceType type, ChessGame.TeamColor color) {
        this.type = type;
        this.color = color;
    }
}
