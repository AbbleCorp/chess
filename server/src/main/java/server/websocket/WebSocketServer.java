package server.websocket;

import chess.ChessMove;
import com.google.gson.*;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import spark.Spark;
import websocket.commands.*;

import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.lang.reflect.Type;

//this one handles gameCommand, deserializes those
@WebSocket
public class WebSocketServer {
    private static Gson serializer;
    private final ConnectionManager connections = new ConnectionManager();

    public static void main() {
        gsonSetup();
        Spark.port(8080);
        Spark.webSocket("/ws", WebSocketServer.class);
    }

    private static void gsonSetup() {
        GsonBuilder builder=new GsonBuilder();
        builder.registerTypeAdapter(UserGameCommand.class, new GameCommandDeserializer());
        serializer=builder.create();
    }


    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws Exception {
        try {
            UserGameCommand command=serializer.fromJson(message, UserGameCommand.class);
            String username = "IMPLEMENT";
            //TODO: find how to get username
            // String username = getUsername(command.getAuthToken()); ?
            switch (command.getCommandType()) {
                case CONNECT -> connect(username, session);
                case LEAVE -> leave(username);
                case MAKE_MOVE -> makeMove(username, (MakeMoveCommand) command);
                case RESIGN -> resign(username);
            }


        } catch (Exception e) {
            session.getRemote().sendString("Error: unauthorized");
        }
    }

    private void connect(String username, Session session) throws IOException {
        connections.add(username, session);
        String playerColor = "IMPLEMENT";
        //TODO: find way to get color?
        var message = String.format("%s has joined the game as %s", username,playerColor);
        var notification = new NotificationMessage(message);
        connections.broadcast(username,notification);
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
            JsonObject jsonObject=jsonElement.getAsJsonObject();
            String typeString=jsonObject.get("commandType").getAsString();
            UserGameCommand.CommandType commandType=UserGameCommand.CommandType.valueOf(typeString);

            return switch (commandType) {
                case CONNECT -> context.deserialize(jsonElement, ConnectCommand.class);
                case LEAVE -> context.deserialize(jsonElement, LeaveCommand.class);
                case MAKE_MOVE -> context.deserialize(jsonElement, MakeMoveCommand.class);
                case RESIGN -> context.deserialize(jsonElement, ResignCommand.class);
            };
        }
    }

}
