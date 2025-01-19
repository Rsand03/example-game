package ee.taltech.examplegame.game;

import ee.taltech.examplegame.network.ServerConnection;
import ee.taltech.examplegame.network.listener.GameStateMessageListener;
import message.GameStateMessage;

public class GameStateReceiver {

    private GameStateMessage latestGameStateMessage;


    public GameStateReceiver() {
        // Listening for updates from the server
        ServerConnection
            .getInstance()
            .getClient()
            .addListener(new GameStateMessageListener(this));
    }

    public GameStateMessage getLatestGameStateMessage() {
        return latestGameStateMessage;
    }

    public void setLatestGameStateMessage(GameStateMessage gameStateMessage) {
        latestGameStateMessage = gameStateMessage;
    }
}
