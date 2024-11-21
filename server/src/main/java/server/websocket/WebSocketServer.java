package server.websocket;

import chess.ChessMove;
import com.google.gson.*;
import dataaccess.AuthDAO;
import dataaccess.DataAccessException;
import dataaccess.GameDAO;
import model.ErrorMessage;
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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//this one handles gameCommand, deserializes those
@WebSocket
public class WebSocketServer {
    private static Gson serializer;
    private final ConnectionManager connections = new ConnectionManager();
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
            switch (command.getCommandType()) {
                case CONNECT -> connect(command.getGameID(),username, session);
                case LEAVE -> leave(username);
                case MAKE_MOVE -> makeMove(username, (MakeMoveCommand) command);
                case RESIGN -> resign(username);
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

    private void isValidGame(int gameID, Session session) throws IOException {
        if (!gameDAO.listGames().contains(gameID)) {
            session.getRemote().sendString(serializer.toJson(new ErrorMessage("Error: Game does not exist.")));
        }
    }

    private void connect(int gameId, String username, Session session) throws IOException, DataAccessException {
        //isValidGame(gameId,session);
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

    private void leave(String username) {
        //TODO: implement
    }

    private void makeMove(String username, MakeMoveCommand command) {
        ChessMove move = command.getMove();
        //TODO: implement
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
