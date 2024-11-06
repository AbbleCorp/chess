import chess.ChessPiece;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class Board {
    private ChessPiece[][] pieceList;
    private PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);

    Board(ChessPiece[][] pieceList) {
        this.pieceList = pieceList;
    }

    public void main(String[] args) {
        out.print(ERASE_SCREEN);



        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    public void DrawBoard() {
        DrawHeader();


        DrawFooter();
    }


    private void DrawHeader() {
        //TODO: implement
        String[] header = {"a","b","c","d","e","f","g"};
        setBorderColor();
        out.print(EMPTY.repeat(3));
    }

    private void DrawFooter() {
        //TODO: implement
    }

    void setBorderColor() {
        out.print(SET_BG_COLOR_DARK_GREEN);
        out.print(SET_TEXT_COLOR_WHITE);
    }
}
