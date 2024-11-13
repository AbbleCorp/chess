package ui;

import chess.ChessGame;
import chess.ChessPiece;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

import static ui.EscapeSequences.*;

public class Board {
    private final ChessPiece[][] pieceList;
    private final PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);


    public Board(ChessPiece[][] pieceList) {
        this.pieceList = pieceList;
    }

    public void main(String[] args) {
        out.print(ERASE_SCREEN);

        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    public void drawBoard(String color) {
        drawHeader(color);
        drawBoardSquares(color);
        drawHeader(color);
        newLine();
        newLine();
    }


    private void drawHeader(String color) {
        String[] header = {" ", "a", "b", "c", "d", "e", "f", "g", "h", " "};
        setBorderColor();
        if (color.equals("WHITE")) {
            for (int i = 0; i < header.length; i++) {
                out.print(" " + header[i] + " ");
            }
        } else {
            for (int i = header.length - 1; i >= 0; i--) {
                out.print(" " + header[i] + " ");
            }
        }
        newLine();
    }

    private void drawBoardSquares(String color) {
        if (color.equals("WHITE")) {
            for (int i = 8; i > 0; i -= 2) {
                drawWhiteRow(color, i);
                drawBlackRow(color, i - 1);
            }
        } else {
            for (int i = 1; i < 9; i += 2) {
                drawWhiteRow(color, i);
                drawBlackRow(color, i + 1);
            }
        }
    }


    private void drawWhiteRow(String color, int i) {
        drawEdge(i);
        if (color.equals("BLACK")) {
        for (int j = 8; j > 0; j -= 2) {
            drawWhiteSquare(i, j);
            drawBlackSquare(i, j - 1);
        }}
        else {
            for (int j = 1; j < 9; j += 2) {
                drawWhiteSquare(i, j);
                drawBlackSquare(i, j + 1);
            }
        }
        drawEdge(i);
        newLine();
    }

    private void drawBlackRow(String color, int i) {
        drawEdge(i);
        if (color.equals("BLACK")) {
            for (int j = 8; j > 0; j -= 2) {
                drawBlackSquare(i, j);
                drawWhiteSquare(i, j - 1);
            }}
        else {
            for (int j = 1; j < 9; j += 2) {
                drawBlackSquare(i, j);
                drawWhiteSquare(i, j + 1);
            }
        }
        drawEdge(i);
        newLine();
    }

    private void drawWhiteSquare(int i, int j) {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(printPiece(i-1,j-1));

    }

    private void drawBlackSquare(int i, int j) {
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(printPiece(i-1,j-1));
    }

    private void drawEdge(int i) {
        setBorderColor();
        out.print(" " + i + " ");
    }

    private String printPiece(int i, int j) {
        if (pieceList[i][j] == null) {
            return EMPTY;
        }
        ChessPiece piece = pieceList[i][j];
        ChessGame.TeamColor color = piece.getTeamColor();
        ChessPiece.PieceType type = piece.getPieceType();
        String pieceText = null;
        if (color == ChessGame.TeamColor.BLACK) {
            out.print(SET_TEXT_COLOR_BLACK);
            switch (type) {
                case PAWN -> pieceText = BLACK_PAWN;
                case KNIGHT -> pieceText = BLACK_KNIGHT;
                case ROOK -> pieceText = BLACK_ROOK;
                case BISHOP -> pieceText = BLACK_BISHOP;
                case QUEEN -> pieceText = BLACK_QUEEN;
                case KING -> pieceText = BLACK_KING;
            }
        }
        else if (color == ChessGame.TeamColor.WHITE) {
            out.print(SET_TEXT_COLOR_WHITE);
            switch(type) {
                case PAWN -> pieceText = WHITE_PAWN;
                case KNIGHT -> pieceText = WHITE_KNIGHT;
                case ROOK -> pieceText = WHITE_ROOK;
                case BISHOP -> pieceText = WHITE_BISHOP;
                case QUEEN -> pieceText = WHITE_QUEEN;
                case KING -> pieceText = WHITE_KING;
            }
        }
        return pieceText;
    }


    void setBorderColor() {
        out.print(SET_BG_COLOR_BLUE);
        out.print(SET_TEXT_COLOR_WHITE);
    }



    void newLine() {
        out.print(RESET_BG_COLOR);
        out.println();
    }


}
