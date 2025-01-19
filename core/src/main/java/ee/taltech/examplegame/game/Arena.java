package ee.taltech.examplegame.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ee.taltech.examplegame.util.Sprites;
import message.BulletState;
import message.GameStateMessage;

import java.util.ArrayList;
import java.util.List;

import static constant.Constants.BULLET_HEIGHT_IN_PIXELS;
import static constant.Constants.BULLET_WIDTH_IN_PIXELS;

public class Arena {

    private final List<Player> players = new ArrayList<>();
    private List<BulletState> bullets = new ArrayList<>();


    public void update(GameStateMessage gameStateMessage) {
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
                BULLET_WIDTH_IN_PIXELS,
                BULLET_HEIGHT_IN_PIXELS
            );
        });
    }

}
