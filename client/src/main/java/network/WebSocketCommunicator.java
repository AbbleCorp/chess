package network;

import com.google.gson.*;
import ui.ServerMessageObserver;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.lang.reflect.Type;

//this one opens the connection, handles server messages
public class WebSocketCommunicator extends Endpoint {
    private ServerMessageObserver SMO;
    private Gson serializer;


    public WebSocketCommunicator(ServerMessageObserver SMO) throws ResponseException {
        this.SMO = SMO;
        gsonSetup();
    }


    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {

    }

    private void gsonSetup() {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(ServerMessage.class, new ServerMessageDeserializer());
        serializer = builder.create();
    }

    private static class ServerMessageDeserializer implements JsonDeserializer<ServerMessage> {

        @Override
        public ServerMessage deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = jsonElement.getAsJsonObject();
            String typeString = jsonObject.get("serverMessageType").getAsString();
            ServerMessage.ServerMessageType messageType = ServerMessage.ServerMessageType.valueOf(typeString);

            return switch (messageType) {
                case ERROR -> context.deserialize(jsonElement, ErrorMessage.class);
                case LOAD_GAME -> context.deserialize(jsonElement, LoadGameMessage.class);
                case NOTIFICATION -> context.deserialize(jsonElement, NotificationMessage.class);
            };
        }
    }
}
