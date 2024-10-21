package chess;

import java.util.ArrayList;
/**
 * A calculator that returns an array list of possible moves according to the
 * type of the piece. Methods for each piece type
 *
 */
public class PieceMoveCalc {
    private ChessPosition pos;
    private ChessGame.TeamColor color;
    private ChessPiece.PieceType type;
    private ChessBoard board;

    public PieceMoveCalc(ChessPosition position, ChessGame.TeamColor color, ChessPiece.PieceType type, ChessBoard board) {
        this.pos = position;
        this.color = color;
        this.type = type;
        this.board = board;
    }

    public ArrayList<ChessMove> calcPieceMoves(ChessPiece.PieceType type) {
        ArrayList<ChessMove> movesList = new ArrayList<ChessMove>();
        switch(type) {
            case KING -> movesList = kingMoves();
            case KNIGHT -> movesList = knightMoves();
            case BISHOP -> movesList = bishopMoves();
            case ROOK -> movesList = rookMoves();
            case QUEEN -> movesList = queenMoves();
            case PAWN -> movesList = pawnMoves();
        }
        return movesList;
    }

    //booleans for checking conditions --withinBounds, spaceEmpty, enemyFound, emptyOrEnemy

    public boolean withinBounds(int row, int col) {
        return (0 < row && row < 9 && 0 < col && col < 9);
    }

    public boolean spaceEmpty(int row, int col) {
        return withinBounds(row,col)&&(board.getPiece(new ChessPosition(row,col))==null);
    }

    public boolean enemyFound(int row, int col) {
        return withinBounds(row,col)&&!spaceEmpty(row,col)&&(board.getPiece(new ChessPosition(row,col)).getTeamColor() != color);
    }

    public boolean emptyOrEnemy(int row, int col) {
        return spaceEmpty(row,col) || enemyFound(row,col);
    }

    public boolean pieceFound(int row, int col) {
        return !(board.getPiece(new ChessPosition(row,col))==null);
    }

    public boolean eligibleForPromo(int row) {
        return row == 1 || row == 8;
    }

    public ArrayList<ChessMove> addPromoMoves(ChessPosition newPosition) {
        ArrayList<ChessMove> movesList = new ArrayList<>();
        movesList.add(new ChessMove(pos, newPosition, ChessPiece.PieceType.QUEEN));
        movesList.add(new ChessMove(pos, newPosition, ChessPiece.PieceType.ROOK));
        movesList.add(new ChessMove(pos, newPosition, ChessPiece.PieceType.KNIGHT));
        movesList.add(new ChessMove(pos, newPosition, ChessPiece.PieceType.BISHOP));
        return movesList;
    }

    public ArrayList<ChessMove> kingMoves() {
        ArrayList<ChessMove> movesList = new ArrayList<>();
        //up
        if (emptyOrEnemy(pos.getRow()+1, pos.getColumn())) {
            movesList.add(new ChessMove(pos, new ChessPosition(pos.getRow()+1,pos.getColumn()),null));
        }//down
        if (emptyOrEnemy(pos.getRow()-1, pos.getColumn())) {
            movesList.add(new ChessMove(pos, new ChessPosition(pos.getRow()-1,pos.getColumn()),null));
        }//left
        if (emptyOrEnemy(pos.getRow(), pos.getColumn()-1)) {
            movesList.add(new ChessMove(pos, new ChessPosition(pos.getRow(),pos.getColumn()-1),null));
        }//right
        if (emptyOrEnemy(pos.getRow(), pos.getColumn()+1)) {
            movesList.add(new ChessMove(pos, new ChessPosition(pos.getRow(), pos.getColumn() + 1), null));
        }//diag
        //upper left
        if (emptyOrEnemy(pos.getRow()+1, pos.getColumn()-1)) {
            movesList.add(new ChessMove(pos, new ChessPosition(pos.getRow()+1,pos.getColumn()-1),null));
        }//upper right
        if (emptyOrEnemy(pos.getRow()+1, pos.getColumn()+1)) {
            movesList.add(new ChessMove(pos, new ChessPosition(pos.getRow()+1,pos.getColumn()+1),null));
        }//lower left
        if (emptyOrEnemy(pos.getRow()-1, pos.getColumn()-1)) {
            movesList.add(new ChessMove(pos, new ChessPosition(pos.getRow()-1,pos.getColumn()-1),null));
        }//lower right
        if (emptyOrEnemy(pos.getRow()-1, pos.getColumn()+1)) {
            movesList.add(new ChessMove(pos, new ChessPosition(pos.getRow()-1,pos.getColumn()+1),null));
        }
        return movesList;
    }

    public ArrayList<ChessMove> knightMoves() {
        ArrayList<ChessMove> movesList = new ArrayList<>();
        //upper left corner
        if (emptyOrEnemy(pos.getRow()+1, pos.getColumn()-2)) {
            movesList.add(new ChessMove(pos, new ChessPosition(pos.getRow()+1,pos.getColumn()-2),null));
        }
        if (emptyOrEnemy(pos.getRow()+2, pos.getColumn()-1)) {
            movesList.add(new ChessMove(pos, new ChessPosition(pos.getRow()+2,pos.getColumn()-1),null));
        }//upper right corner
        if (emptyOrEnemy(pos.getRow()+1, pos.getColumn()+2)) {
            movesList.add(new ChessMove(pos, new ChessPosition(pos.getRow()+1,pos.getColumn()+2),null));
        }
        if (emptyOrEnemy(pos.getRow()+2, pos.getColumn()+1)) {
            movesList.add(new ChessMove(pos, new ChessPosition(pos.getRow()+2,pos.getColumn()+1),null));
        }//lower left corner
        if (emptyOrEnemy(pos.getRow()-1, pos.getColumn()-2)) {
            movesList.add(new ChessMove(pos, new ChessPosition(pos.getRow()-1,pos.getColumn()-2),null));
        }
        if (emptyOrEnemy(pos.getRow()-2, pos.getColumn()-1)) {
            movesList.add(new ChessMove(pos, new ChessPosition(pos.getRow()-2,pos.getColumn()-1),null));
        }//lower right corner
        if (emptyOrEnemy(pos.getRow()-1, pos.getColumn()+2)) {
            movesList.add(new ChessMove(pos, new ChessPosition(pos.getRow()-1,pos.getColumn()+2),null));
        }
        if (emptyOrEnemy(pos.getRow()-2, pos.getColumn()+1)) {
            movesList.add(new ChessMove(pos, new ChessPosition(pos.getRow()-2,pos.getColumn()+1),null));
        }
        return movesList;
    }

    public ArrayList<ChessMove> bishopMoves() {
        ArrayList<ChessMove> movesList = new ArrayList<>();
        //upper left
        int i = 1;
        while (emptyOrEnemy(pos.getRow()+i,pos.getColumn()-i)) {
            movesList.add(new ChessMove(pos,new ChessPosition(pos.getRow()+i,pos.getColumn()-i),null));
            if (pieceFound(pos.getRow()+i,pos.getColumn()-i)) {break;}
            i++;
        }//upper right
        i = 1;
        while (emptyOrEnemy(pos.getRow()+i,pos.getColumn()+i)) {
            movesList.add(new ChessMove(pos,new ChessPosition(pos.getRow()+i,pos.getColumn()+i),null));
            if (pieceFound(pos.getRow()+i,pos.getColumn()+i)) {break;}
            i++;
        }//lower left
        i = 1;
        while (emptyOrEnemy(pos.getRow()-i,pos.getColumn()-i)) {
            movesList.add(new ChessMove(pos,new ChessPosition(pos.getRow()-i,pos.getColumn()-i),null));
            if (pieceFound(pos.getRow()-i,pos.getColumn()-i)) {break;}
            i++;
        }//lower right
        i = 1;
        while (emptyOrEnemy(pos.getRow()-i,pos.getColumn()+i)) {
            movesList.add(new ChessMove(pos,new ChessPosition(pos.getRow()-i,pos.getColumn()+i),null));
            if (pieceFound(pos.getRow()-i,pos.getColumn()+i)) {break;}
            i++;
        }
        return movesList;
    }

    public ArrayList<ChessMove> rookMoves() {
        ArrayList<ChessMove> movesList = new ArrayList<>();
        //up
        int i = 1;
        while (emptyOrEnemy(pos.getRow()+i,pos.getColumn())) {
            movesList.add(new ChessMove(pos,new ChessPosition(pos.getRow()+i,pos.getColumn()),null));
            if (pieceFound(pos.getRow()+i,pos.getColumn())) {break;}
            i++;
        }//down
        i = 1;
        while (emptyOrEnemy(pos.getRow()-i,pos.getColumn())) {
            movesList.add(new ChessMove(pos,new ChessPosition(pos.getRow()-i,pos.getColumn()),null));
            if (pieceFound(pos.getRow()-i,pos.getColumn())) {break;}
            i++;
        }//left
        i = 1;
        while (emptyOrEnemy(pos.getRow(),pos.getColumn()-i)) {
            movesList.add(new ChessMove(pos,new ChessPosition(pos.getRow(),pos.getColumn()-i),null));
            if (pieceFound(pos.getRow(),pos.getColumn()-i)) {break;}
            i++;
        }//right
        i = 1;
        while (emptyOrEnemy(pos.getRow(),pos.getColumn()+i)) {
            movesList.add(new ChessMove(pos,new ChessPosition(pos.getRow(),pos.getColumn()+i),null));
            if (pieceFound(pos.getRow(),pos.getColumn()+i)) {break;}
            i++;
        }
        return movesList;
    }

    public ArrayList<ChessMove> queenMoves() {
        ArrayList<ChessMove> movesList = bishopMoves();
        movesList.addAll(rookMoves());
        return movesList;
    }

    public ArrayList<ChessMove> pawnMoves() {
        ArrayList<ChessMove> movesList = new ArrayList<>();
        //BLACK
        if (color == ChessGame.TeamColor.BLACK) {
            //eligible for Promo
            if (eligibleForPromo(pos.getRow()-1)) {
                //promo moves
                //straight ahead
                if (spaceEmpty(pos.getRow()-1,pos.getColumn())) {
                    movesList.addAll(addPromoMoves(new ChessPosition(pos.getRow()-1,pos.getColumn())));
                }// capture enemy lower left
                if (enemyFound(pos.getRow()-1,pos.getColumn()-1)) {
                    movesList.addAll(addPromoMoves(new ChessPosition(pos.getRow()-1,pos.getColumn()-1)));
                }//capture enemy lower right
                if (enemyFound(pos.getRow()-1,pos.getColumn()+1)) {
                    movesList.addAll(addPromoMoves(new ChessPosition(pos.getRow()-1,pos.getColumn()+1)));
                }
            } else { //normal moves
                //first move?
                if(pos.getRow()==7&&spaceEmpty(pos.getRow()-1,pos.getColumn())&&spaceEmpty(pos.getRow()-2,pos.getColumn())) {
                    movesList.add(new ChessMove(pos,new ChessPosition(pos.getRow()-2,pos.getColumn()),null));
                }//straight down
                if (spaceEmpty(pos.getRow()-1,pos.getColumn())) {
                    movesList.add(new ChessMove(pos,new ChessPosition(pos.getRow()-1,pos.getColumn()),null));
                }//capture enemy lower left
                if (enemyFound(pos.getRow()-1,pos.getColumn()-1)) {
                    movesList.add(new ChessMove(pos,new ChessPosition(pos.getRow()-1,pos.getColumn()-1),null));
                }//capture enemy lower right
                if (enemyFound(pos.getRow()-1,pos.getColumn()+1)) {
                    movesList.add(new ChessMove(pos,new ChessPosition(pos.getRow()-1,pos.getColumn()+1),null));
                }
            }
        } else {
            //WHITE
            //eligible for promo
            if (eligibleForPromo(pos.getRow()+1)) {
                //promo moves
                //straight ahead
                if (spaceEmpty(pos.getRow()+1,pos.getColumn())) {
                    movesList.addAll(addPromoMoves(new ChessPosition(pos.getRow()+1,pos.getColumn())));
                }// capture enemy upper left
                if (enemyFound(pos.getRow()+1,pos.getColumn()-1)) {
                    movesList.addAll(addPromoMoves(new ChessPosition(pos.getRow()+1,pos.getColumn()-1)));
                }//capture enemy upper right
                if (enemyFound(pos.getRow()+1,pos.getColumn()+1)) {
                    movesList.addAll(addPromoMoves(new ChessPosition(pos.getRow()+1,pos.getColumn()+1)));
                }
            } else { //normal moves
                //first move?
                if(pos.getRow()==2&&spaceEmpty(pos.getRow()+1,pos.getColumn())&&spaceEmpty(pos.getRow()+2,pos.getColumn())) {
                    movesList.add(new ChessMove(pos,new ChessPosition(pos.getRow()+2,pos.getColumn()),null));
                }//straight up
                if (spaceEmpty(pos.getRow()+1,pos.getColumn())) {
                    movesList.add(new ChessMove(pos,new ChessPosition(pos.getRow()+1,pos.getColumn()),null));
                }//capture enemy upper left
                if (enemyFound(pos.getRow()+1,pos.getColumn()-1)) {
                    movesList.add(new ChessMove(pos,new ChessPosition(pos.getRow()+1,pos.getColumn()-1),null));
                }//capture enemy upper right
                if (enemyFound(pos.getRow()+1,pos.getColumn()+1)) {
                    movesList.add(new ChessMove(pos,new ChessPosition(pos.getRow()+1,pos.getColumn()+1),null));
                }
            }
        }

        return movesList;
    }
}
