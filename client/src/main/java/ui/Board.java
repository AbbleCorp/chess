package ui;

import chess.ChessGame;
import chess.ChessMove;
import chess.ChessPiece;
import chess.ChessPosition;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collection;

import static ui.EscapeSequences.*;

public class Board {
    private ChessPiece[][] pieceList;
    private boolean[][] validMoves;
    private ChessGame game;
    private final PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);


    public Board(ChessGame game) {
        this.pieceList = game.getBoard().getBoard();
        validMoves = new boolean[8][8];
        this.game = game;
    }

    public void main(String[] args) {
        out.print(ERASE_SCREEN);
        out.print(SET_BG_COLOR_BLACK);
        out.print(SET_TEXT_COLOR_WHITE);
    }

    public void drawBoard(String color) {
        newLine();
        drawHeader(color);
        drawBoardSquares(color);
        drawHeader(color);
        newLine();
        newLine();
    }


    private void setValidMoves(ChessPosition pos) {
        Collection<ChessMove> moves = game.validMoves(pos);
        for (var move : moves) {
            validMoves[move.getEndPosition().getRow()-1][move.getEndPosition().getColumn()-1] = true;
        }
    }

    public void highlightLegalMoves(String color, ChessPosition pos) {
        setValidMoves(pos);
        newLine();
        drawHeader(color);
        drawHighlightedSquares(color,pos);
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

    private void drawHighlightedSquares(String color, ChessPosition pos) {
        if (color.equals("WHITE")) {
            for (int i = 8; i > 0; i -= 2) {
                drawHighlightedWhiteRow(color, i,pos);
                drawHighlightedBlackRow(color, i - 1,pos);
            }
        } else {
            for (int i = 1; i < 9; i += 2) {
                drawHighlightedWhiteRow(color, i,pos);
                drawHighlightedBlackRow(color, i + 1,pos);
            }
        }
    }

    private void drawHighlightedWhiteRow(String color, int i, ChessPosition pos) {
        drawEdge(i);
        if (color.equals("BLACK")) {
            for (int j = 8; j > 0; j -= 2) {
                drawHighlightedWhiteSquare(i, j,pos);
                drawHighlightedBlackSquare(i, j - 1,pos);
            }
        } else {
            for (int j = 1; j < 9; j += 2) {
                drawHighlightedWhiteSquare(i, j,pos);
                drawHighlightedBlackSquare(i, j + 1,pos);
            }
        }
        drawEdge(i);
        newLine();
    }

    private void drawHighlightedBlackRow(String color, int i, ChessPosition pos) {
        drawEdge(i);
        if (color.equals("BLACK")) {
            for (int j = 8; j > 0; j -= 2) {
                drawHighlightedBlackSquare(i, j, pos);
                drawHighlightedWhiteSquare(i, j - 1,pos);
            }
        } else {
            for (int j = 1; j < 9; j += 2) {
                drawHighlightedBlackSquare(i, j,pos);
                drawHighlightedWhiteSquare(i, j + 1,pos);
            }
        }
        drawEdge(i);
        newLine();
    }

    private void drawHighlightedWhiteSquare(int i, int j, ChessPosition pos) {
        if (validMoves[i-1][j-1] == true) {
            setLightGreen();
        } else if ((pos.getRow() == i) && (pos.getColumn()==j)) {
            setYellow();
        } else {
        out.print(SET_BG_COLOR_LIGHT_GREY);
        out.print(RESET_TEXT_BOLD_FAINT);
        }
        out.print(printPiece(i - 1, j - 1));

    }

    private void drawHighlightedBlackSquare(int i, int j, ChessPosition pos) {
        if (validMoves[i-1][j-1] == true) {
            setDarkGreen();
        } else if ((pos.getRow() == i) && (pos.getColumn()==j)) {
            setYellow();
        } else {
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(RESET_TEXT_BOLD_FAINT); }
        out.print(printPiece(i - 1, j - 1));
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
            }
        } else {
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
            }
        } else {
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
        out.print(printPiece(i - 1, j - 1));

    }

    private void drawBlackSquare(int i, int j) {
        out.print(SET_BG_COLOR_DARK_GREY);
        out.print(printPiece(i - 1, j - 1));
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
        } else if (color == ChessGame.TeamColor.WHITE) {
            out.print(SET_TEXT_COLOR_WHITE);
            switch (type) {
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
        resetColor();
        out.println();
    }

    void resetColor() {
        out.print(RESET_BG_COLOR);
        out.print(RESET_TEXT_COLOR);
    }

    void setDarkGreen() {
        out.print(SET_BG_COLOR_DARK_GREEN);
        out.print(SET_TEXT_BOLD);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    void setLightGreen() {
        out.print(SET_BG_COLOR_GREEN);
        out.print(SET_TEXT_BOLD);
        out.print(SET_TEXT_COLOR_BLACK);
    }

    void setYellow() {
        out.print(SET_BG_COLOR_YELLOW);
        out.print(SET_TEXT_BOLD);
        out.print(SET_TEXT_COLOR_BLACK);
    }


}
