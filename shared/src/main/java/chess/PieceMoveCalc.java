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

    public boolean eligibleForPromotion(int row) {
        return row == 1 || row == 8;
    }

    public boolean pieceNotFound(int row, int col) {
        return chessboard.getPiece(new ChessPosition(row, col)) == null;
    }

    public boolean enemyFound(int row, int col) {
        return ((chessboard.getPiece(new ChessPosition(row,col)) != null) && (chessboard.getPiece(new ChessPosition(row,col)).getTeamColor() != color));
    }

    public void addPromoMoves(ArrayList<ChessMove> MovesList, int row, int col) {
        MovesList.add(new ChessMove(pos, (new ChessPosition(row,col)), ChessPiece.PieceType.QUEEN));
        MovesList.add(new ChessMove(pos, (new ChessPosition(row,col)), ChessPiece.PieceType.ROOK));
        MovesList.add(new ChessMove(pos, (new ChessPosition(row,col)), ChessPiece.PieceType.KNIGHT));
        MovesList.add(new ChessMove(pos, (new ChessPosition(row,col)), ChessPiece.PieceType.BISHOP));
    }

    //nested static class?
    //need to take into account
    public ArrayList<ChessMove> PawnMovesCalc() {
        ArrayList<ChessMove> MovesList = new ArrayList<ChessMove>();
        //pawns at top of board
        if (color == ChessGame.TeamColor.BLACK) {
            //check for double move on first move
            if (pos.getRow() == 7 && pieceNotFound(6,pos.getColumn()) && pieceNotFound(5,pos.getColumn())) {
                MovesList.add(new ChessMove(pos, (new ChessPosition(pos.getRow()-2,pos.getColumn())),null));
            } //move one
            if (WithinBounds(pos.getRow()-1,pos.getColumn()) && pieceNotFound(pos.getRow()-1,pos.getColumn())) {
                //check eligible for promotion, if true then add 4 moves with promotion pieces
                if (eligibleForPromotion(pos.getRow()-1)) {
                    addPromoMoves(MovesList, pos.getRow()-1,pos.getColumn());
                } else { //add without promotion piece
                    MovesList.add(new ChessMove(pos, (new ChessPosition(pos.getRow()-1,pos.getColumn())), null));
                }
            } //check diagonals --possible if enemy found
            // </
            if (WithinBounds(pos.getRow()-1,pos.getColumn()-1) && enemyFound(pos.getRow()-1,pos.getColumn()-1)) {
                if (eligibleForPromotion(pos.getRow()-1)) {
                    addPromoMoves(MovesList, pos.getRow()-1,pos.getColumn()-1);
                } else {
                    MovesList.add(new ChessMove(pos, (new ChessPosition(pos.getRow()-1,pos.getColumn()-1)),null));
                }
            }
            // \>
            if (WithinBounds(pos.getRow()-1,pos.getColumn()+1) &&enemyFound(pos.getRow()-1,pos.getColumn()+1)) {
                if (eligibleForPromotion(pos.getRow()-1)) {
                    addPromoMoves(MovesList, pos.getRow()-1,pos.getColumn()+1);
                } else {
                    MovesList.add(new ChessMove(pos, (new ChessPosition(pos.getRow()-1,pos.getColumn()+1)),null));
                }
            }

        }
        else { //color is WHITE, pawns at bottom of board
            //check for double move on first move
            if (pos.getRow() == 2 && pieceNotFound(3, pos.getColumn()) && pieceNotFound(4, pos.getColumn())) {
                MovesList.add(new ChessMove(pos, (new ChessPosition(pos.getRow() + 2, pos.getColumn())), null));
            }
            //move one
            if (WithinBounds(pos.getRow()+1,pos.getColumn()) &&pieceNotFound(pos.getRow()+1, pos.getColumn())) {
                //check eligible for promotion, if true then add 4 moves with promotion pieces
                if (eligibleForPromotion(pos.getRow()+1)) {
                    addPromoMoves(MovesList, pos.getRow()+1, pos.getColumn());
                } else { //add without promotion piece
                    MovesList.add(new ChessMove(pos, (new ChessPosition(pos.getRow()+1, pos.getColumn())), null));
                }
            }

            //check diagonals --possible if enemy found
            // <\
            if (WithinBounds(pos.getRow()+1,pos.getColumn()-1) && enemyFound(pos.getRow()+1,pos.getColumn()-1)) {
                if (eligibleForPromotion(pos.getRow()+1)) {
                    addPromoMoves(MovesList, pos.getRow()+1,pos.getColumn()-1);
                } else {
                    MovesList.add(new ChessMove(pos, (new ChessPosition(pos.getRow()+1,pos.getColumn()-1)),null));
                }
            }
            // />
            if (WithinBounds(pos.getRow()+1,pos.getColumn()+1) &&enemyFound(pos.getRow()+1,pos.getColumn()+1)) {
                if (eligibleForPromotion(pos.getRow()+1)) {
                    addPromoMoves(MovesList, pos.getRow()+1,pos.getColumn()+1);
                } else {
                    MovesList.add(new ChessMove(pos, (new ChessPosition(pos.getRow()+1,pos.getColumn()+1)),null));
                }
            }
        }
        return MovesList;
    }


    public ArrayList<ChessMove> RookMovesCalc() {
        ArrayList<ChessMove> MovesList = new ArrayList<ChessMove>();
        //up
        int i = 1;
        while (spaceAvailable(pos.getRow()+i,pos.getColumn())) {
            MovesList.add(new ChessMove(pos, new ChessPosition(pos.getRow()+i,pos.getColumn()),null));
            if (pieceFound(pos.getRow()+i,pos.getColumn())) break;
            i++;
        }
        //down
        i = 1;
        while (spaceAvailable(pos.getRow()-i,pos.getColumn())) {
            MovesList.add(new ChessMove(pos, new ChessPosition(pos.getRow()-i,pos.getColumn()),null));
            if (pieceFound(pos.getRow()-i,pos.getColumn())) break;
            i++;
        }
        //left
        i = 1;
        while (spaceAvailable(pos.getRow(),pos.getColumn()-i)) {
            MovesList.add(new ChessMove(pos, new ChessPosition(pos.getRow(),pos.getColumn()-i),null));
            if (pieceFound(pos.getRow(),pos.getColumn()-i)) break;
            i++;
        }
        //right
        i = 1;
        while (spaceAvailable(pos.getRow(),pos.getColumn()+i)) {
            MovesList.add(new ChessMove(pos, new ChessPosition(pos.getRow(),pos.getColumn()+i),null));
            if (pieceFound(pos.getRow(),pos.getColumn()+i)) break;
            i++;
        }
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
        ArrayList<ChessMove> MovesList = RookMovesCalc();
        MovesList.addAll(BishopMovesCalc());
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
