package ee.taltech.examplegame.server.game;

import com.esotericsoftware.kryonet.Connection;
import ee.taltech.examplegame.server.listener.PlayerMovementListener;
import lombok.Getter;
import lombok.Setter;
import message.PlayerState;

import static constant.Constants.PLAYER_LIVES_COUNT;
import static constant.Constants.PLAYER_SPEED;

@Getter
@Setter
public class Player {
    private final Connection connection;
    private float x, y = 0f;
    private int lives = PLAYER_LIVES_COUNT;

    public Player(Connection connection) {
        this.connection = connection;
        this.connection.addListener(new PlayerMovementListener(this));
    }

    public void move(message.Direction direction) {
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
}
