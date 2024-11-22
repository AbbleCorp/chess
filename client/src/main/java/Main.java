import chess.*;
import network.ResponseException;
import ui.Board;
import ui.Client;

public class Main {
    public static void main(String[] args) throws Exception {
//        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
//        System.out.println("♕ 240 Chess ui.Client: " + piece);
        Client client = new Client();
        client.preLoginMenu();

    }
}