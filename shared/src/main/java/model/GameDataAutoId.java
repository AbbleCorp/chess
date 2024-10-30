package model;

import chess.ChessGame;

public record GameDataAutoId(String whiteUsername, String blackUsername, String gameName, ChessGame game) {
}
