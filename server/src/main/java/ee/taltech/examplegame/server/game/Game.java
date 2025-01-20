package ee.taltech.examplegame.server.game;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.minlog.Log;
import ee.taltech.examplegame.server.listener.ServerListener;
import lombok.Getter;
import message.BulletState;
import message.GameStateMessage;
import message.PlayerState;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import static constant.Constants.GAME_TICK_RATE;
import static constant.Constants.PLAYER_COUNT_IN_GAME;

/**
 * Represents the game logic and server-side management of the game instance.
 * Handles player connections, game state updates, bullet collisions, and communication with clients.
 */
public class Game extends Thread {

    private final ServerListener server;
    private final BulletCollisionHandler collisionHandler = new BulletCollisionHandler();

    private final List<Connection> connections = new ArrayList<>();
    private final List<Player> players = new ArrayList<>();
    private List<Bullet> bullets = new ArrayList<>();
    @Getter
    private boolean isGameRunning = false;
    private boolean allPlayersHaveJoined = false;
    private float gameTime = 0;

    /**
     * Initializes the game instance.
     *
     * @param server Reference to ServerListener to call dispose() when the game is finished or all players leave.
     */
    public Game(ServerListener server) {
        this.server = server;
    }


    public void addBullet(Bullet bullet) {
        this.bullets.add(bullet);
    }

    /**
     * Check if the game has the required number of players to start.
     */
    public boolean hasEnoughPlayers() {
        return connections.size() == PLAYER_COUNT_IN_GAME;
    }

    /**
     * Adds a new connection and player to the game.
     * If the required number of players is reached, the game is ready to start.
     *
     * @param connection Connection to the client side of the player.
     */
    public void addConnection(Connection connection) {
        if (hasEnoughPlayers()) {
            Log.info("Cannot add connection: Required number of players already connected.");
            return;
        }

        // Add new player and connection
        Player newPlayer = new Player(connection, this);
        players.add(newPlayer);
        connections.add(connection);

        // Check if the game is ready to start
        if (hasEnoughPlayers()) {
            allPlayersHaveJoined = true;
        }
    }

    public void removeConnection(Connection connection) {
        this.connections.remove(connection);
    }

    /**
     * Game loop. Updates the game state, checks for collisions, and sends updates to clients.
     * The game loop runs until the game is stopped or no players remain.
     */
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

            // If a player is dead, end the game
            if (players.stream().anyMatch(x -> x.getLives() == 0)) {
                // Use TCP to ensure that the last gameStateMessage reaches all clients
                connections.forEach(connection -> connection.sendTCP(gameStateMessage));
                players.forEach(Player::dispose); // remove movement and shooting listeners
                connections.clear();
                server.disposeGame();
                isGameRunning = false;
            }

            // If no players are connected, stop the game loop
            if (connections.isEmpty()) {
                Log.info("No players connected, stopping game loop.");
                isGameRunning = false;
            }

            try {
                // We don't want to update the game state every millisecond, that would be
                // too much for the server to handle. So a tick rate is used to limit the
                // amount of updates per second.
                Thread.sleep(Duration.ofMillis(1000 / GAME_TICK_RATE));
            } catch (InterruptedException e) {
                Log.error("Game loop sleep interrupted", e);
            }
        }
    }
}
