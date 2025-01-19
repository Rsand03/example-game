package ee.taltech.examplegame.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ee.taltech.examplegame.network.ServerConnection;
import ee.taltech.examplegame.network.listener.GameStateMessageListener;
import ee.taltech.examplegame.util.Sprites;
import message.BulletState;
import message.GameStateMessage;

import java.util.ArrayList;
import java.util.List;

public class Arena {
    private final List<Player> players = new ArrayList<>();
    private List<BulletState> bullets = new ArrayList<>();
    private GameStateMessage latestGameStateMessage;

    public Arena() {
        // listening for updates from the server
        ServerConnection
            .getInstance()
            .getClient()
            .addListener(new GameStateMessageListener(this));
    }

    public void updateGameState(GameStateMessage gameStateMessage) {
        latestGameStateMessage = gameStateMessage;
        gameStateMessage.getPlayerStates().forEach(playerState -> {
            var player = players
                .stream()
                .filter(p -> p.getId() == playerState.getId())
                .findFirst()
                .orElseGet(() -> {
                    var newPlayer = new Player(playerState.getId());
                    players.add(newPlayer);
                    return newPlayer;
                });

            player.setX(playerState.getX());
            player.setY(playerState.getY());
        });

        this.bullets = gameStateMessage.getBulletStates();
    }

    public void render(float delta, SpriteBatch spriteBatch) {
        // render all players
        players.forEach(player -> {
            player.render(delta, spriteBatch);
        });

        // render all bullets
        bullets.forEach(bullet -> {
            spriteBatch.draw(
                Sprites.bulletTexture,
                bullet.getX(),
                bullet.getY(),
                8,
                8
            );
        });
    }

    public GameStateMessage getLatestGameStateMessage() {
        return latestGameStateMessage;
    }
}
