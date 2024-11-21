package server.websocket;

import chess.*;
import com.google.gson.*;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import websocket.messages.ErrorMessage;
import model.GameData;
import model.UserData;
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

//this one handles gameCommand, deserializes those
@WebSocket
public class WebSocketServer {
    private static Gson serializer;
    private final GameDAO gameDAO;
    private final AuthDAO authDAO;
    private Map<Integer, List<Session>> openGames = new HashMap<>();


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
                case RESIGN -> resign(username);
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
            if (client.isOpen() && !client.equals(session)) {
                client.getRemote().sendString(serializer.toJson(message));
            }}
    }


    private String getPlayerColor(int gameID, String username) throws DataAccessException {
        GameData game = gameDAO.getGame(gameID);
        String playerColor = "";
        if (game.whiteUsername().equals(username)) {
            playerColor = "white";
        } else if (game.blackUsername().equals(username)) {
            playerColor = "black";
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



    private void connect(int gameId, String username, Session session) throws IOException, DataAccessException {
        try {
            addToOpenGames(gameId, session);
            String playerColor = getPlayerColor(gameId, username);
            var message = String.format("%s has joined the game as %s", username, playerColor);
            var notification = new NotificationMessage(message);
            broadcastToOthers(gameId, notification, session);
            session.getRemote().sendString(serializer.toJson(new LoadGameMessage(gameDAO.getGame(gameId))));
        }
        catch (Exception e) {
            throw new DataAccessException("Error: Game does not exist");
        }
    }


    private void leave(int gameId, String username, Session session) throws DataAccessException, IOException {
        try {
        openGames.get(gameId).remove(session);
        GameData game = gameDAO.getGame(gameId);
        GameData newGame = null;
        if (username.equals(game.whiteUsername())) {
            newGame = new GameData(gameId,null,game.blackUsername(),game.gameName(),game.game());
        } else if (username.equals(game.blackUsername())) {
            newGame = new GameData(gameId, game.whiteUsername(), null,game.gameName(),game.game());
        }
        gameDAO.updateGame(gameId, newGame);
        NotificationMessage notification = new NotificationMessage(String.format("%s has left %s.",username,game.gameName()));
        broadcastToOthers(gameId,notification,session); }
        catch (Exception e){
            throw new DataAccessException("Error: game does not exist.");
        }
    }

    private String moveToString(ChessPosition pos) {
        String posString = null;
        switch (pos.getRow()) {
            case 1 -> posString = "a" + pos.getColumn();
            case 2 -> posString = "b" + pos.getColumn();
            case 3 -> posString = "c" + pos.getColumn();
            case 4 -> posString = "d" + pos.getColumn();
            case 5 -> posString = "e" + pos.getColumn();
            case 6 -> posString = "f" + pos.getColumn();
            case 7 -> posString = "g" + pos.getColumn();
            case 8 -> posString = "h" + pos.getColumn();
        }
        return posString;
    }

    private String makeMoveString(String username, ChessMove move, ChessPiece piece) {
        String pieceType = piece.getPieceType().toString();
        String startPos = moveToString(move.getStartPosition());
        String endPos = moveToString(move.getEndPosition());
        return String.format("%s has moved %s from %s to %s.", username, pieceType, startPos,endPos);
    }

    private boolean turnColorMatches(GameData gameData, ChessPiece piece, String username) throws DataAccessException {
        ChessGame game = gameData.game();
        String playerColor = getPlayerColor(gameData.gameID(),username);
        String teamTurn = game.getTeamTurn().toString().toLowerCase();
        return (game.getTeamTurn().equals(piece.getTeamColor())) && (playerColor.equals(teamTurn));
    }

    private void makeMove(MakeMoveCommand command, String username, Session session) throws DataAccessException, InvalidMoveException, IOException {
        ChessMove move = command.getMove();
        GameData gameData = gameDAO.getGame(command.getGameID());
        ChessGame game = gameData.game();
        Collection<ChessMove> validMoves = game.validMoves(move.getStartPosition());
        ChessPiece piece = game.getBoard().getPiece(move.getStartPosition());
        if (validMoves.contains(move) && turnColorMatches(gameData,piece,username)) {
            game.makeMove(move);
        } else {
            throw new InvalidMoveException("Error: Invalid Move");
        }
        GameData newGameData = new GameData(command.getGameID(),gameData.whiteUsername(), gameData.blackUsername(), gameData.gameName(), game);
        gameDAO.updateGame(command.getGameID(), newGameData);
        LoadGameMessage loadGame = new LoadGameMessage(newGameData);
        broadcastAll(command.getGameID(), loadGame);
        String message = makeMoveString(username,move,piece);
        NotificationMessage notif = new NotificationMessage(message);
        broadcastToOthers(command.getGameID(),notif, session);
    }

    private void resign(String username) {
        //TODO: implement
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
