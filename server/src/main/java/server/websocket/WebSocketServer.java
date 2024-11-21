package server.websocket;

import com.google.gson.*;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import spark.Spark;
import websocket.commands.*;

import javax.websocket.*;
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
            //String username = getUsername(command.getAuthToken()); how to get username?
            switch (command.getCommandType()) {
                case CONNECT -> connect(username, session);
                case LEAVE -> leave(username);
                case MAKE_MOVE -> makeMove();
                case RESIGN -> resign();
            }


        } catch (Exception e) {

        }
    }

    private void connect(String username, Session session) {

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
