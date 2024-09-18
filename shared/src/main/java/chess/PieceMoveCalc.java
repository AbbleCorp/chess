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

    public boolean pieceFound(int row, int col) {
        if (chessboard.getPiece(new ChessPosition(row, col)) != null) {
            return true;
        } else return false;
    }

    public boolean WithinBounds(int row, int col) {
        return row > 0 && row <= 8 && col > 0 && col <= 8; // if potential move would be out of bounds, return false
    }

    //checks if space is empty or if the piece is from opposing team
    public boolean moveable(int row, int col) {
        ChessPosition position = new ChessPosition(row, col);
        if (chessboard.getPiece(position) == null || chessboard.getPiece(position).getTeamColor() != color) {
            return true;
        } else return false;
    }

    public boolean spaceAvailable(int row, int col) {
        return WithinBounds(row, col) && moveable(row, col);
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
        //top left corner
        //up 2 left 1
        if (spaceAvailable(pos.getRow()+2,pos.getColumn()-1)) {
            MovesList.add(new ChessMove(pos, new ChessPosition(pos.getRow()+2,pos.getColumn()-1),null));
        } //up 1 left 2
        if (spaceAvailable(pos.getRow()+1,pos.getColumn()-2)) {
            MovesList.add(new ChessMove(pos, new ChessPosition(pos.getRow()+1,pos.getColumn()-2),null));
        }
        //top right corner
        //up 2 right 1
        if (spaceAvailable(pos.getRow()+2,pos.getColumn()+1)) {
            MovesList.add(new ChessMove(pos, new ChessPosition(pos.getRow()+2,pos.getColumn()+1),null));
        }//up 1 right 2
        if (spaceAvailable(pos.getRow()+1,pos.getColumn()+2)) {
            MovesList.add(new ChessMove(pos, new ChessPosition(pos.getRow()+1,pos.getColumn()+2),null));
        }
        // bottom left corner
        // down 2 left 1
        if (spaceAvailable(pos.getRow()-2,pos.getColumn()-1)) {
            MovesList.add(new ChessMove(pos, new ChessPosition(pos.getRow()-2,pos.getColumn()-1),null));
        }
        // down 1 left 2
        if (spaceAvailable(pos.getRow()-1,pos.getColumn()-2)) {
            MovesList.add(new ChessMove(pos, new ChessPosition(pos.getRow()-1,pos.getColumn()-2),null));
        }
        // bottom right corner
        // down 2 right 1
        if (spaceAvailable(pos.getRow()-2,pos.getColumn()+1)) {
            MovesList.add(new ChessMove(pos, new ChessPosition(pos.getRow()-2,pos.getColumn()+1),null));
        }
        // down 1 right 2
        if (spaceAvailable(pos.getRow()-1,pos.getColumn()+2)) {
            MovesList.add(new ChessMove(pos, new ChessPosition(pos.getRow()-1,pos.getColumn()+2),null));
        }
        return MovesList;
    }

    public ArrayList<ChessMove> BishopMovesCalc() {
        ArrayList<ChessMove> MovesList = new ArrayList<ChessMove>();
        //top left
        int i = 1;
        while (spaceAvailable(pos.getRow()+i,pos.getColumn()-i)) {
            MovesList.add(new ChessMove(pos, new ChessPosition(pos.getRow()+i,pos.getColumn()-i),null));
            if (pieceFound(pos.getRow()+i,pos.getColumn()-i)) break;
            i++;
            }
        //top right
        i = 1;
        while (spaceAvailable(pos.getRow()+i,pos.getColumn()+i)) {
            MovesList.add(new ChessMove(pos, new ChessPosition(pos.getRow()+i,pos.getColumn()+i),null));
            if (pieceFound(pos.getRow()+i,pos.getColumn()+i)) break;
            i++;
        }
        //bottom left
        i = 1;
        while (spaceAvailable(pos.getRow()-i,pos.getColumn()-i)) {
            MovesList.add(new ChessMove(pos, new ChessPosition(pos.getRow()-i,pos.getColumn()-i),null));
            if (pieceFound(pos.getRow()-i,pos.getColumn()-i)) break;
            i++;
        }
        //bottom right
        i = 1;
        while (spaceAvailable(pos.getRow()-i,pos.getColumn()+i)) {
            MovesList.add(new ChessMove(pos, new ChessPosition(pos.getRow()-i,pos.getColumn()+i),null));
            if (pieceFound(pos.getRow()-i,pos.getColumn()+i)) break;
            i++;
        }
        return MovesList;
    }

    public ArrayList<ChessMove> QueenMovesCalc() {
        ArrayList<ChessMove> MovesList = new ArrayList<ChessMove>();

        return MovesList;
    }

    public ArrayList<ChessMove> KingMovesCalc() {
        ArrayList<ChessMove> MovesList = new ArrayList<ChessMove>();
        //vertical moves
        //up
        if (spaceAvailable(pos.getRow()+1,pos.getColumn())) {
            MovesList.add(new ChessMove(pos, new ChessPosition(pos.getRow()+1,pos.getColumn()),null));
        } //down
        if (spaceAvailable(pos.getRow()-1,pos.getColumn())) {
            MovesList.add(new ChessMove(pos, new ChessPosition(pos.getRow()-1,pos.getColumn()),null));
        }
        //horizontal moves
        //right
        if (spaceAvailable(pos.getRow(),pos.getColumn()+1)) {
            MovesList.add(new ChessMove(pos, new ChessPosition(pos.getRow(),pos.getColumn()+1),null));
        }//left
        if (spaceAvailable(pos.getRow(),pos.getColumn()-1)) {
            MovesList.add(new ChessMove(pos, new ChessPosition(pos.getRow(),pos.getColumn()-1),null));
        }
        // diagonal moves
        // <^
        if (spaceAvailable(pos.getRow()+1,pos.getColumn()-1)) {
            MovesList.add(new ChessMove(pos, new ChessPosition(pos.getRow()+1,pos.getColumn()-1),null));
        }
        //>^
        if (spaceAvailable(pos.getRow()+1,pos.getColumn()+1)) {
            MovesList.add(new ChessMove(pos, new ChessPosition(pos.getRow()+1,pos.getColumn()+1),null));
        }
        //<\/
        if (spaceAvailable(pos.getRow()-1,pos.getColumn()-1)) {
            MovesList.add(new ChessMove(pos, new ChessPosition(pos.getRow()-1,pos.getColumn()-1),null));
        }
        //>\/
        if (spaceAvailable(pos.getRow()-1,pos.getColumn()+1)) {
            MovesList.add(new ChessMove(pos, new ChessPosition(pos.getRow()-1,pos.getColumn()+1),null));
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
