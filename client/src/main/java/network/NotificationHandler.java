package network;
import websocket.messages.NotificationMessage;

public interface NotificationHandler {
  void notify(NotificationMessage notification);
}
