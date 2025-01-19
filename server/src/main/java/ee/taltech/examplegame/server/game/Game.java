package ee.taltech.examplegame.server.game;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.minlog.Log;
import lombok.Getter;
import message.BulletState;
import message.GameStateMessage;
import message.PlayerState;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static constant.Constants.GAME_TICK_RATE;

public class Game extends Thread {

    private static final int LOBBY_SIZE = 2;  // TODO set when creating a new game

    private final BulletCollisionHandler collisionHandler = new BulletCollisionHandler();
    private final List<Connection> connections = new ArrayList<>();
    private final List<Player> players = new ArrayList<>();
    private List<Bullet> bullets = new ArrayList<>();
    @Getter
    private boolean isGameRunning = false;
    private boolean allPlayersHaveJoined = false;
    private float gameTime = 0;


    public void addBullet(Bullet bullet) {
        this.bullets.add(bullet);
    }

    public void addConnection(Connection connection) {
        var player = new Player(connection, this);

        this.players.add(player);
        this.connections.add(connection);

        if (connections.size() == LOBBY_SIZE) {
            allPlayersHaveJoined = true;
        }
    }

    public void removeConnection(Connection connection) {
        this.connections.remove(connection);
    }

    @Override
    public void run() {
        isGameRunning = true;

        while (isGameRunning) {
            if (allPlayersHaveJoined) {
                gameTime += 1f / GAME_TICK_RATE;
            }
            // update bullets, check for collisions and remove out of bounds bullets
            bullets.forEach(Bullet::update);
            bullets = collisionHandler.handleCollisions(bullets, players);

            // get the state of all players
            var playerStates = new ArrayList<PlayerState>();
            players.forEach(player -> playerStates.add(player.getState()));

            // get state of all bullets
            var bulletStates = new ArrayList<BulletState>();
            bullets.forEach(bullet -> bulletStates.add(bullet.getState()));

            var gameStateMessage = new GameStateMessage();
            gameStateMessage.setPlayerStates(playerStates);
            gameStateMessage.setBulletStates(bulletStates);
            gameStateMessage.setGameTime(Math.round(gameTime));
            gameStateMessage.setAllPlayersHaveJoined(allPlayersHaveJoined);

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
