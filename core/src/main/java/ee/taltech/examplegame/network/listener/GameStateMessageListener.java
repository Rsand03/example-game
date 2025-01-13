package ee.taltech.examplegame.network.listener;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import ee.taltech.examplegame.game.Arena;
import message.GameStateMessage;

public class GameStateMessageListener extends Listener {
    private final Arena arena;

    public GameStateMessageListener(Arena arena) {
        this.arena = arena;
    }

    @Override
    public void received(Connection connection, Object object) {
        super.received(connection, object);

        if (object instanceof GameStateMessage gameStateMessage) {
            // update the game state
            arena.updateGameState(gameStateMessage);
        }
    }
}
