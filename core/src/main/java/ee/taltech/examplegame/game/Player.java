package ee.taltech.examplegame.game;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ee.taltech.examplegame.util.Sprites;

import static constant.Constants.PLAYER_HEIGHT_IN_PIXELS;
import static constant.Constants.PLAYER_WIDTH_IN_PIXELS;

public class Player {
    private final int id;
    private float x;
    private float y;

    public Player(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setX(float x) {
        this.x = x;
    }

    public void setY(float y) {
        this.y = y;
    }

    public void render(SpriteBatch spriteBatch) {
        // draw the player sprite
        spriteBatch.draw(Sprites.playerTexture, x, y, PLAYER_WIDTH_IN_PIXELS, PLAYER_HEIGHT_IN_PIXELS);
    }
}
