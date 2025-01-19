package ee.taltech.examplegame.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ee.taltech.examplegame.game.Arena;
import ee.taltech.examplegame.game.Input;
import ee.taltech.examplegame.screen.overlay.Hud;

public class GameScreen extends ScreenAdapter {
    private final Arena arena;
    private final Input input;
    private final Hud hud;
    private final SpriteBatch spriteBatch;

    public GameScreen(Game game) {
        input = new Input();

        spriteBatch = new SpriteBatch();
        arena = new Arena();
        hud = new Hud(arena);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        // clear the screen
        Gdx.gl.glClearColor(192 / 255f, 192 / 255f, 192 / 255f, 1); // to get that win 95 look
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        input.handleMovementInput();
        input.handleShootingInput();

        // all rendering should happen between spriteBatch.begin() and spriteBatch.end()
        spriteBatch.begin();
        arena.render(delta, spriteBatch);  // players, bullets
        hud.render();  // info overlay with names, lives etc
        spriteBatch.end();

    }
}
