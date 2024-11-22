package websocket.commands;

public class ConnectCommand extends UserGameCommand {
    private JoinType joinType;

    public ConnectCommand(String authToken, Integer gameID, JoinType joinType) {
        super(CommandType.CONNECT, authToken, gameID);
        this.joinType = joinType;
    }

    public enum JoinType {
        PLAYER,
        OBSERVER
    }

    public JoinType getJoinType() {
        return joinType;
    }
}
