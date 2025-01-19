package ee.taltech.examplegame.game;

import ee.taltech.examplegame.network.ServerConnection;
import ee.taltech.examplegame.network.listener.GameStateMessageListener;
import message.GameStateMessage;

import java.util.ArrayList;

public class GameStateManager {

    private GameStateMessage latestGameStateMessage;


    public GameStateManager() {
        // Listening for updates from the server
        ServerConnection
            .getInstance()
            .getClient()
            .addListener(new GameStateMessageListener(this));

        // Initialize latestGameStateMessage to prevent NullPointerExceptions
        latestGameStateMessage = new GameStateMessage();
        latestGameStateMessage.setPlayerStates(new ArrayList<>());
        latestGameStateMessage.setBulletStates(new ArrayList<>());
        latestGameStateMessage.setGameTime(0);
        latestGameStateMessage.setAllPlayersHaveJoined(false);
    }

    public GameStateMessage getLatestGameStateMessage() {
        return latestGameStateMessage;
    }

    public void setLatestGameStateMessage(GameStateMessage gameStateMessage) {
        latestGameStateMessage = gameStateMessage;
    }
}
