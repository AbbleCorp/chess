package chess;
/**
 * A calculator that returns an array list of possible moves according to the
 * type of the piece. Subclasses for each piece type.
 */
public class PieceMoveCalc {
    private ChessPiece.PieceType type;

    PieceMoveCalc(ChessPiece.PieceType type) {
        this.type = type;
    }
}
