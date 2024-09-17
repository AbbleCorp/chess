package chess;

import java.util.ArrayList;
import java.util.Objects;

/**
 * A calculator that returns an array list of possible moves according to the
 * type of the piece. Subclasses for each piece type.
 *
 * Static Nested Classes??
 */
public class PieceMoveCalc {
    private ChessPiece.PieceType type;
    private ChessGame.TeamColor color;
    private ChessPosition pos;
    private ChessBoard chessboard;

    PieceMoveCalc(ChessPosition pos, ChessPiece.PieceType type, ChessGame.TeamColor color, ChessBoard board) {
        this.type = type;
        this.color = color;
        this.pos = pos;
        this.chessboard = board;
    }

    public ArrayList<ChessMove> CalcPieceMoves() {
        ArrayList<ChessMove> MovesList = new ArrayList<>();
        switch (type) {
            case PAWN -> MovesList = PawnMovesCalc();
            case ROOK -> MovesList = RookMovesCalc();
            case KNIGHT -> MovesList = KnightMovesCalc();
            case BISHOP -> MovesList = BishopMovesCalc();
            case KING -> MovesList = KingMovesCalc();
            case QUEEN -> MovesList = QueenMovesCalc();
        }
        return MovesList;
    }

    public boolean WithinBounds(int row, int col) {
        return row >= 0 && row < 8 && col >= 0 && col < 8; // if potential move would be out of bounds, return false
    }

    //check color of piece if opponent or not
    public boolean PosEmpty(int row, int col) {
        ChessPosition position = new ChessPosition(row, col);
        if (chessboard.getPiece(position) == null) {
            return true;
        } else return false;
    }

    public boolean EligibleForPromotion(int row) {
        return row == 0 || row == 7;
    }

    //nested static class?
    //need to take into account
    public ArrayList<ChessMove> PawnMovesCalc() {
        ArrayList<ChessMove> MovesList = new ArrayList<ChessMove>();

        return MovesList;
    }


    public ArrayList<ChessMove> RookMovesCalc() {
        ArrayList<ChessMove> MovesList = new ArrayList<ChessMove>();

        return MovesList;
    }

    public ArrayList<ChessMove> KnightMovesCalc() {
        ArrayList<ChessMove> MovesList = new ArrayList<ChessMove>();

        return MovesList;
    }

    public ArrayList<ChessMove> BishopMovesCalc() {
        ArrayList<ChessMove> MovesList = new ArrayList<ChessMove>();

        return MovesList;
    }

    public ArrayList<ChessMove> QueenMovesCalc() {
        ArrayList<ChessMove> MovesList = new ArrayList<ChessMove>();

        return MovesList;
    }

    public ArrayList<ChessMove> KingMovesCalc() {
        ArrayList<ChessMove> MovesList = new ArrayList<ChessMove>();
        //vertical moves
        if (WithinBounds(pos.getRow()+1,pos.getColumn())) {
            MovesList.add(new ChessMove(pos, new ChessPosition(pos.getRow()+1,pos.getColumn()),null));
        }
        if (WithinBounds(pos.getRow()-1,pos.getColumn())) {
            MovesList.add(new ChessMove(pos, new ChessPosition(pos.getRow()-1,pos.getColumn()),null));
        }
        //horizontal moves
        if (WithinBounds(pos.getRow(),pos.getColumn()+1)) {
            MovesList.add(new ChessMove(pos, new ChessPosition(pos.getRow(),pos.getColumn()+1),null));
        }
        if (WithinBounds(pos.getRow(),pos.getColumn()-1)) {
            MovesList.add(new ChessMove(pos, new ChessPosition(pos.getRow(),pos.getColumn()-1),null));
        }
        // diagonal moves
        // <^
        if (WithinBounds(pos.getRow()+1,pos.getColumn()-1)) {
            MovesList.add(new ChessMove(pos, new ChessPosition(pos.getRow()+1,pos.getColumn()-1),null));
        }
        //>^
        if (WithinBounds(pos.getRow()+1,pos.getColumn()+1)) {
            MovesList.add(new ChessMove(pos, new ChessPosition(pos.getRow()+1,pos.getColumn()+1),null));
        }
        //<\/
        if (WithinBounds(pos.getRow()-1,pos.getColumn()-1)) {
            MovesList.add(new ChessMove(pos, new ChessPosition(pos.getRow()+1,pos.getColumn()-1),null));
        }
        //>\/
        if (WithinBounds(pos.getRow()-1,pos.getColumn()+1)) {
            MovesList.add(new ChessMove(pos, new ChessPosition(pos.getRow()+1,pos.getColumn()-1),null));
        }
        return MovesList;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        PieceMoveCalc that = (PieceMoveCalc) o;
        return type == that.type && color == that.color && Objects.equals(pos, that.pos) && Objects.equals(chessboard, that.chessboard);
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, color, pos, chessboard);
    }
}
