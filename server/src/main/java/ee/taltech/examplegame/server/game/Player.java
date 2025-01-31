package ee.taltech.examplegame.server.game;

import com.esotericsoftware.kryonet.Connection;
import ee.taltech.examplegame.server.listener.PlayerMovementListener;
import ee.taltech.examplegame.server.listener.PlayerShootingListener;
import lombok.Getter;
import lombok.Setter;
import message.dto.Direction;
import message.dto.PlayerState;

import static constant.Constants.*;

/**
 * Server-side representation of a player in the game. This class listens for player movements or shooting actions
 * and changes the player's server-side state accordingly. Lives management.
 */
@Getter
@Setter
public class Player {
    private final Connection connection;
    // Keep track of listener objects for each player connection, so they can be disposed when the game ends
    private final PlayerMovementListener movementListener = new PlayerMovementListener(this);
    private final PlayerShootingListener shootingListener = new PlayerShootingListener(this);

    private final int id;
    private final Game game;
    private float x, y = 0f;
    private int lives = PLAYER_LIVES_COUNT;

    /**
     * Initializes a new server-side representation of a Player with a game reference and connection to client-side.
     *
     * @param connection Connection to client-side.
     * @param game Game instance that this player is a part of.
     */
    public Player(Connection connection, Game game) {
        this.connection = connection;
        this.id = connection.getID();
        this.game = game;
        this.connection.addListener(movementListener);
        this.connection.addListener(shootingListener);
    }

    /**
     * Moves the player in the specified direction within the arena bounds.
     *
     * @param direction The direction in which the player moves.
     */
    public void move(Direction direction) {
        if (direction == null) return;

        switch (direction) {
            case UP -> y += 1 * PLAYER_SPEED;
            case DOWN -> y -= 1 * PLAYER_SPEED;
            case LEFT -> x -= 1 * PLAYER_SPEED;
            case RIGHT -> x += 1 * PLAYER_SPEED;
        }

        // enforce arena bounds
        x = Math.max(ARENA_LOWER_BOUND_X, Math.min(x, ARENA_UPPER_BOUND_X - PLAYER_WIDTH_IN_PIXELS));
        y = Math.max(ARENA_LOWER_BOUND_Y, Math.min(y, ARENA_UPPER_BOUND_Y - PLAYER_HEIGHT_IN_PIXELS));

    }

    /**
     * Returns the current state of the player, consisting of their position and remaining lives.
     */
    public PlayerState getState() {
        PlayerState playerState = new PlayerState();
        playerState.setId(connection.getID());
        playerState.setX(x);
        playerState.setY(y);
        playerState.setLives(lives);
        return playerState;
    }

    public void shoot(Direction direction) {
        // adjust bullet spawn position to be in the center of player
        game.addBullet(
            new Bullet(x + PLAYER_WIDTH_IN_PIXELS / 2, y + PLAYER_HEIGHT_IN_PIXELS / 2, direction, id)
        );
    }

    public void decreaseLives() {
        if (lives > 0) {
            setLives(getLives() - 1);
        }
    }

    /**
     * Removes the movement and shooting listeners from the player's connection.
     * This should be called when the player disconnects or the game ends.
     * Disposing of the listeners prevents potential thread exceptions when reusing
     * same connections for future game instances.
     */
    public void dispose() {
        connection.removeListener(movementListener);
        connection.removeListener(shootingListener);
    }

}
