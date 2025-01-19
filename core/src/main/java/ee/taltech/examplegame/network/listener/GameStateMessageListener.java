package ee.taltech.examplegame.network.listener;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import ee.taltech.examplegame.game.GameStateManager;
import message.GameStateMessage;

public class GameStateMessageListener extends Listener {

    private final GameStateManager remoteManager;


    public GameStateMessageListener(GameStateManager gameStateManager) {
        this.remoteManager = gameStateManager;
    }

    @Override
    public void received(Connection connection, Object object) {
        super.received(connection, object);

        if (object instanceof GameStateMessage gameStateMessage) {
            // Update the game state
            remoteManager.setLatestGameStateMessage(gameStateMessage);
        }
    }
}
