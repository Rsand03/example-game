package ee.taltech.examplegame.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ee.taltech.examplegame.util.Sprites;
import message.BulletState;
import message.GameStateMessage;

import java.util.ArrayList;
import java.util.List;

import static constant.Constants.ARENA_UPPER_BOUND_X;
import static constant.Constants.ARENA_UPPER_BOUND_Y;
import static constant.Constants.BULLET_HEIGHT_IN_PIXELS;
import static constant.Constants.BULLET_WIDTH_IN_PIXELS;
import static ee.taltech.examplegame.util.Sprites.taltechMapTexture;

/**
 * Initialize a new Arena, which is responsible for updating and rendering the following: players, bullets, map.
 */
public class Arena {

    private final List<Player> players = new ArrayList<>();
    private List<BulletState> bullets = new ArrayList<>();

    /**
     * Update players and bullets, so they are later rendered in the correct position.
     *
     * @param gameStateMessage latest game state received from the server
     */
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

    /**
     * Render map, players and bullets. This makes them  visible on the screen.
     *
     * @param delta       time since last frame
     * @param spriteBatch used for rendering (and syncing) all visual elements
     */
    public void render(float delta, SpriteBatch spriteBatch) {
        // render map
        spriteBatch.draw(taltechMapTexture, 0, 0, ARENA_UPPER_BOUND_X, ARENA_UPPER_BOUND_Y);

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
