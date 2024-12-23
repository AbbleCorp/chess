package server.websocket;

import chess.*;
import com.google.gson.*;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import websocket.messages.ErrorMessage;
import model.GameData;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import websocket.commands.*;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;

import static chess.ChessGame.TeamColor.WHITE;
import static chess.ChessGame.TeamColor.BLACK;

//this one handles gameCommand, deserializes those
@WebSocket
public class WebSocketServer {
    private static Gson serializer;
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;
    private final Map<Integer, List<Session>> openGames = new HashMap<>();


    public WebSocketServer(GameDAO gameDAO, AuthDAO authDAO) {
        this.gameDAO = gameDAO;
        this.authDAO = authDAO;
        gsonSetup();
    }

    private static void gsonSetup() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(UserGameCommand.class, new GameCommandDeserializer());
        serializer = builder.create();
    }


    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        try {
            UserGameCommand command = serializer.fromJson(message, UserGameCommand.class);
            String username = authDAO.getUsername(command.getAuthToken());
            if (username != null) {
            switch (command.getCommandType()) {
                case CONNECT -> connect(command.getGameID(),username, session);
                case LEAVE -> leave(command.getGameID(), username,session);
                case MAKE_MOVE -> makeMove((MakeMoveCommand) command, username, session);
                case RESIGN -> resign(command.getGameID(),username,session);
            } } else {
                throw new Exception("Error: unauthorized.");
            }
        } catch (Exception e) {
            ErrorMessage errorMessage = new ErrorMessage(e.getMessage());
            session.getRemote().sendString(serializer.toJson(errorMessage));
        }
    }

    private void broadcastAll(int gameId, ServerMessage message) throws IOException {
        for (var client : openGames.get(gameId)) {
            if (client.isOpen()) {
                client.getRemote().sendString(serializer.toJson(message));
            }}
    }

    private void broadcastToOthers(int gameId, ServerMessage message, Session session) throws IOException {
        for (var client : openGames.get(gameId)) {
            boolean clientOpen = client.isOpen();
            if (clientOpen && !client.equals(session)) {
                client.getRemote().sendString(serializer.toJson(message));
            }}
    }




    private String getPlayerColor(int gameID, String username) throws DataAccessException {
        GameData game = gameDAO.getGame(gameID);
        String playerColor = "";
        if (username.equals(game.whiteUsername())) {
            playerColor = "WHITE";
        } else if (username.equals(game.blackUsername())) {
            playerColor = "BLACK";
        }
        return playerColor;
    }

    private void addToOpenGames(int gameId, Session session) {
        if (openGames.containsKey(gameId)) {
            openGames.get(gameId).add(session);
        } else {
            List<Session> sessionList = new ArrayList<>();
            sessionList.add(session);
            openGames.put(gameId,sessionList);
        }
    }


    private boolean isPlayer(int gameId, String username) throws DataAccessException {
        GameData game = gameDAO.getGame(gameId);
        return username.equals(game.whiteUsername()) || username.equals(game.blackUsername());
    }


    private void connect(int gameId, String username, Session session) throws DataAccessException {
        try {
            addToOpenGames(gameId, session);
            String message;
            if (isPlayer(gameId,username)) {
            String playerColor = getPlayerColor(gameId, username);
            message = String.format("%s has joined the game as %s.", username, playerColor); }
            else  {
                message = String.format("%s is observing the game.",username);
            }
            var notification = new NotificationMessage(message);
            broadcastToOthers(gameId, notification, session);
            session.getRemote().sendString(serializer.toJson(new LoadGameMessage(gameDAO.getGame(gameId))));
        }
        catch (DataAccessException e) {
            throw new DataAccessException("Error: Game does not exist");
        }
        catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }


    private void leave(int gameId, String username, Session session) throws DataAccessException {
        try {
        GameData game = gameDAO.getGame(gameId);
        GameData newGame;
        if (username.equals(game.whiteUsername())) {
            newGame = new GameData(gameId,null,game.blackUsername(),game.gameName(),game.game());
            gameDAO.updateGame(gameId, newGame);
        } else if (username.equals(game.blackUsername())) {
            newGame = new GameData(gameId, game.whiteUsername(), null,game.gameName(),game.game());
            gameDAO.updateGame(gameId, newGame);
        }
        NotificationMessage notification = new NotificationMessage(String.format("%s has left %s.",
                username,game.gameName()));
        broadcastToOthers(gameId,notification,session);
        openGames.get(gameId).remove(session);
        }
        catch (Exception e){
            throw new DataAccessException("Error: game does not exist. (caught at WSS.leave)");
        }
    }

    private String moveToString(ChessPosition pos) {
        String posString = null;
        switch (pos.getColumn()) {
            case 1 -> posString = "a" + pos.getRow();
            case 2 -> posString = "b" + pos.getRow();
            case 3 -> posString = "c" + pos.getRow();
            case 4 -> posString = "d" + pos.getRow();
            case 5 -> posString = "e" + pos.getRow();
            case 6 -> posString = "f" + pos.getRow();
            case 7 -> posString = "g" + pos.getRow();
            case 8 -> posString = "h" + pos.getRow();
        }
        return posString;
    }

    private String makeMoveString(String username, ChessMove move, ChessPiece piece) {
        String pieceType = piece.getPieceType().toString();
        String startPos = moveToString(move.getStartPosition());
        String endPos = moveToString(move.getEndPosition());
        return String.format("%s has moved their %s from %s to %s.",
                username, pieceType.toLowerCase(), startPos,endPos);
    }

    private boolean turnColorMatches(GameData gameData, ChessPiece piece, String username) throws DataAccessException {
        ChessGame game = gameData.game();
        String playerColor = getPlayerColor(gameData.gameID(),username);
        String teamTurn = game.getTeamTurn().toString();
        return (game.getTeamTurn().equals(piece.getTeamColor())) && (playerColor.equals(teamTurn));
    }

    private String getOtherPlayer(GameData gameData, String playerColor) {
        String otherPlayer = null;
        if (playerColor.equals("WHITE")) {otherPlayer = gameData.blackUsername();}
        else if (playerColor.equals("BLACK")) {otherPlayer = gameData.whiteUsername();}
        return otherPlayer;
    }

    private String gameStatusMessage(GameData gameData, String username) throws DataAccessException {
        String statusMessage = null;
        ChessGame game = gameData.game();
        String playerColor = getPlayerColor(gameData.gameID(), username);
        ChessGame.TeamColor otherTeamColor = null;
        String otherUser = getOtherPlayer(gameData,playerColor);
        if (playerColor.equals("WHITE")) {
            otherTeamColor = BLACK;}
        else if (playerColor.equals("BLACK")) {
            otherTeamColor = WHITE;}
        if (game.isInStalemate(otherTeamColor)) {
            statusMessage = "The game is at a stalemate.";
            game.setGameOver(true);
            GameData newGameData = new GameData(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(),
                    gameData.gameName(), game);
            gameDAO.updateGame(gameData.gameID(), newGameData);
        } else if (game.isInCheckmate(otherTeamColor)) {
            statusMessage = String.format("%s is in checkmate.",otherUser);
            game.setGameOver(true);
            GameData newGameData = new GameData(gameData.gameID(), gameData.whiteUsername(), gameData.blackUsername(),
                    gameData.gameName(), game);
            gameDAO.updateGame(gameData.gameID(), newGameData);
        } else if (game.isInCheck(otherTeamColor)) {
            statusMessage = String.format("%s is in check.",otherUser); }
        return statusMessage;
    }

    private void makeMove(MakeMoveCommand command, String username, Session session) throws DataAccessException,
            InvalidMoveException, IOException {
        ChessMove move = command.getMove();
        GameData gameData = gameDAO.getGame(command.getGameID());
        ChessGame game = gameData.game();
        Collection<ChessMove> validMoves = game.validMoves(move.getStartPosition());
        ChessPiece piece = game.getBoard().getPiece(move.getStartPosition());
        if (validMoves != null && validMoves.contains(move) &&
                isPlayer(command.getGameID(),username)
                && turnColorMatches(gameData, game.getBoard().getPiece(move.getStartPosition()),username)) {
            game.makeMove(move);
            GameData newGameData = new GameData(command.getGameID(),gameData.whiteUsername(),
                    gameData.blackUsername(), gameData.gameName(), game);
            gameDAO.updateGame(command.getGameID(), newGameData);
            LoadGameMessage loadGame = new LoadGameMessage(newGameData);
            broadcastAll(command.getGameID(), loadGame);
            String message = makeMoveString(username,move,piece);
            NotificationMessage notif = new NotificationMessage(message);
            broadcastToOthers(command.getGameID(),notif, session);
            String statusMessage = gameStatusMessage(newGameData,username);
        if (statusMessage != null) {
            broadcastAll(command.getGameID(), new NotificationMessage(statusMessage));
        } } else {
            String message = null;
            if (isInCheckmateOrStalemate(game)) {message = "Error: The game is already over.";}
            else if (validMoves == null || !validMoves.contains(move)) {message = "Error: Invalid move.";}
            else if (!isPlayer(command.getGameID(),username)) { message = "Error: Observers cannot move pieces."; }
            else if (!turnColorMatches(gameData,game.getBoard().getPiece(move.getStartPosition()),username))
                    {message = "Error: It is not your turn."; }
            session.getRemote().sendString(serializer.toJson(new ErrorMessage(message)));
        }}

        private boolean isInCheckmateOrStalemate(ChessGame game) {
            return (game.isInCheckmate(WHITE) || game.isInStalemate(WHITE)
                    || game.isInCheckmate(BLACK) || game.isInStalemate(BLACK));
        }




    private boolean validPlayer(GameData gameData, String username) {
        return (username.equals(gameData.blackUsername()) || username.equals(gameData.whiteUsername()));
    }

    private void resign(int gameId, String username, Session session) throws DataAccessException, IOException {
        GameData gameData = gameDAO.getGame(gameId);
        ChessGame game = gameData.game();
        try {
            if (validPlayer(gameData,username) && !game.isGameOver()) {
                game.setGameOver(true);
        GameData newGameData = new GameData(gameId,gameData.whiteUsername(),gameData.blackUsername(),
                gameData.gameName(), game);
        gameDAO.updateGame(gameId,newGameData);
        NotificationMessage notif = new NotificationMessage(String.format("%s has resigned.",username));
        broadcastAll(gameId,notif);
            } else {
                String message = "Error: ";
                if (!validPlayer(gameData,username)) {message += "Observers cannot resign from the game.";}
                else if (game.isGameOver()) {message += "The game has already ended.";}
                session.getRemote().sendString(serializer.toJson(new ErrorMessage(message)));
            }
        }
        catch (Exception e){
            session.getRemote().sendString(serializer.toJson(new ErrorMessage(e.getMessage())));
        }
    }

    private static class GameCommandDeserializer implements JsonDeserializer<UserGameCommand> {
        @Override
        public UserGameCommand deserialize(JsonElement jsonElement, Type type,
                                           JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String typeString = jsonObject.get("commandType").getAsString();
            UserGameCommand.CommandType commandType = UserGameCommand.CommandType.valueOf(typeString);

            return switch (commandType) {
                case CONNECT -> context.deserialize(jsonElement, ConnectCommand.class);
                case LEAVE -> context.deserialize(jsonElement, LeaveCommand.class);
                case MAKE_MOVE -> context.deserialize(jsonElement, MakeMoveCommand.class);
                case RESIGN -> context.deserialize(jsonElement, ResignCommand.class);
            };
        }
    }

}
