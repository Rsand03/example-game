package ee.taltech.examplegame.server.game;

import com.esotericsoftware.kryonet.Connection;
import ee.taltech.examplegame.server.listener.PlayerMovementListener;
import ee.taltech.examplegame.server.listener.PlayerShootingListener;
import lombok.Getter;
import lombok.Setter;
import message.Direction;
import message.PlayerState;

import static constant.Constants.*;

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

    public Player(Connection connection, Game game) {
        this.connection = connection;
        this.id = connection.getID();
        this.game = game;
        this.connection.addListener(movementListener);
        this.connection.addListener(shootingListener);
    }

    public void move(message.Direction direction) {
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

    public void dispose() {
        connection.removeListener(movementListener);
        connection.removeListener(shootingListener);
    }

}
