package ee.taltech.examplegame.network.listener;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import ee.taltech.examplegame.game.GameStateManager;
import message.GameStateMessage;

public class GameStateMessageListener extends Listener {

    private final GameStateManager gameStateManager;


    public GameStateMessageListener(GameStateManager gameStateManager) {
        this.gameStateManager = gameStateManager;
    }

    @Override
    public void received(Connection connection, Object object) {
        super.received(connection, object);

        if (object instanceof GameStateMessage gameStateMessage) {
            // Update the game state
            gameStateManager.setLatestGameStateMessage(gameStateMessage);
        }
    }
}
