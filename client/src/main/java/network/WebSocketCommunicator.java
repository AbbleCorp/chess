package network;

import ui.ServerMessageObserver;

import javax.websocket.*;

//this one opens the connection, handles server messages
public class WebSocketCommunicator extends Endpoint {
  private ServerMessageObserver SMO;


  public WebSocketCommunicator(ServerMessageObserver SMO) throws ResponseException {
    this.SMO = SMO;
  }


  @Override
  public void onOpen(Session session, EndpointConfig endpointConfig) {

  }
}
