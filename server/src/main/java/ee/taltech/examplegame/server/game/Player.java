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
    private final Game game;
    private float x, y = 0f;
    private int lives = PLAYER_LIVES_COUNT;

    public Player(Connection connection, Game game) {
        this.connection = connection;
        this.game = game;
        this.connection.addListener(new PlayerMovementListener(this));
        this.connection.addListener(new PlayerShootingListener(this));
    }

    public void move(message.Direction direction) {
        if (direction == null) return;

        switch (direction) {
            case UP -> y += 1 * PLAYER_SPEED;
            case DOWN -> y -= 1 * PLAYER_SPEED;
            case LEFT -> x -= 1 * PLAYER_SPEED;
            case RIGHT -> x += 1 * PLAYER_SPEED;
        }
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
        game.addBullet(new Bullet(x + PLAYER_WIDTH_IN_PIXELS / 2, y + PLAYER_HEIGHT_IN_PIXELS / 2, direction));
    }
}
