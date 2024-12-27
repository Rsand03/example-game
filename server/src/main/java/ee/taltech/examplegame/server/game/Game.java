package ee.taltech.examplegame.server.game;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.minlog.Log;
import lombok.Getter;
import message.GameStateMessage;
import message.PlayerState;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static constant.Constants.GAME_TICK_RATE;

public class Game extends Thread {
    private final List<Connection> connections = new ArrayList<>();
    private final List<Player> players = new ArrayList<>();
    @Getter
    private boolean isGameRunning = false;

    public void addConnection(Connection connection) {
        var player = new Player(connection);

        this.players.add(player);
        this.connections.add(connection);
    }

    public void removeConnection(Connection connection) {
        this.connections.remove(connection);
    }

    @Override
    public void start() {
        super.start();

        isGameRunning = true;

        while (isGameRunning) {
            // get the state of all players
            var playerStates = new ArrayList<PlayerState>();
            players.forEach(player -> playerStates.add(player.getState()));
            var gameStateMessage = new GameStateMessage();
            gameStateMessage.setPlayerStates(playerStates);

            // send the state of all players to all clients
            connections.forEach(connection -> connection.sendUDP(gameStateMessage));

            // if no players are connected, stop the game loop
            if (connections.isEmpty()) {
                Log.info("No players connected, stopping game loop.");
                isGameRunning = false;
            }

            try {
                // we don't want to update the game state every millisecond that would be
                // too much for the server to handle. So a tick rate is used to limit the
                // amount of updates per second.
                Thread.sleep(Duration.ofMillis(1000 / GAME_TICK_RATE));
            } catch (InterruptedException e) {
                Log.error("Game loop sleep interrupted", e);
            }
        }
    }
}
