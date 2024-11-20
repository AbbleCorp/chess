package network;

import javax.websocket.*;

import com.google.gson.Gson;
import websocket.messages.*;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebSocketHandler extends Endpoint {



  Session session;
  NotificationHandler notificationHandler;


  public void WebSocketFacade(String url, NotificationHandler notificationHandler) throws ResponseException {
    try {
      URI uri = new URI("ws://localhost:8080/ws");
      WebSocketContainer container = ContainerProvider.getWebSocketContainer();
      this.session = container.connectToServer(this, uri);
      this.notificationHandler = notificationHandler;


      //set message handler
      this.session.addMessageHandler(new MessageHandler.Whole<String>() {
        @Override
        public void onMessage(String message) {
          NotificationMessage notification = new Gson().fromJson(message, NotificationMessage.class);
          notificationHandler.notify(notification);
        }
      });
    } catch (DeploymentException | IOException | URISyntaxException ex) {
      throw new ResponseException(500, ex.getMessage());
    }
  }


  @Override
  public void onOpen(Session session, EndpointConfig endpointConfig) {

  }
}
