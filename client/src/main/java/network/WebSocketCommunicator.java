package network;

import com.google.gson.*;
import ui.ServerMessageObserver;
import websocket.commands.UserGameCommand;
import websocket.messages.ErrorMessage;
import websocket.messages.LoadGameMessage;
import websocket.messages.NotificationMessage;
import websocket.messages.ServerMessage;

import javax.websocket.*;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;

//this one opens the connection, handles server messages
public class WebSocketCommunicator extends Endpoint {
    private ServerMessageObserver messageObserver;
    private Gson serializer;
    public Session session;

    public WebSocketCommunicator(ServerMessageObserver messageObserver) throws ResponseException, URISyntaxException, DeploymentException, IOException {
        this.messageObserver = messageObserver;
        gsonSetup();
        URI uri = new URI("ws://localhost:8080/ws");
        WebSocketContainer container = ContainerProvider.getWebSocketContainer();
        this.session = container.connectToServer(this, uri);

        this.session.addMessageHandler(new MessageHandler.Whole<String>() {
            @Override
            public void onMessage(String message) {
                try {
                    ServerMessage serverMessage = serializer.fromJson(message, ServerMessage.class);
                    messageObserver.notify(serverMessage);
                }
                catch (Exception e) {
                    messageObserver.notify(new ErrorMessage(e.getMessage()));
                }
            }
        });
    }

    private void send(String msg) throws Exception {
        this.session.getBasicRemote().sendText(msg);
    }

    public void sendMessage(UserGameCommand command) throws Exception {
        String message = serializer.toJson(command);
        send(message);
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
