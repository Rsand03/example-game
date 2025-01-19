package ee.taltech.examplegame.network.listener;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import ee.taltech.examplegame.game.GameStateReceiver;
import message.GameStateMessage;

public class GameStateMessageListener extends Listener {

    private final GameStateReceiver remoteManager;


    public GameStateMessageListener(GameStateReceiver arena) {
        this.remoteManager = arena;
    }

    @Override
    public void received(Connection connection, Object object) {
        super.received(connection, object);

        if (object instanceof GameStateMessage gameStateMessage) {
            // update the game state
            remoteManager.setLatestGameStateMessage(gameStateMessage);
        }
    }
}
