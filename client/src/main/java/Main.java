import chess.*;

public class Main {
    public static void main(String[] args) {
//        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
//        System.out.println("♕ 240 Chess Client: " + piece);
        Client client = new Client();
        ChessBoard game = new ChessBoard();
        game.resetBoard();
        Board board = new Board(game.getBoard());
        board.drawBoard("WHITE");
//        board.drawBoard("BLACK");
    }
}