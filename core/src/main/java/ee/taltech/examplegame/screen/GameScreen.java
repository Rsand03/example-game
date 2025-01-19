package ee.taltech.examplegame.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ee.taltech.examplegame.game.Arena;
import ee.taltech.examplegame.game.InputManager;
import ee.taltech.examplegame.game.GameStateReceiver;
import ee.taltech.examplegame.screen.overlay.Hud;
import message.GameStateMessage;

public class GameScreen extends ScreenAdapter {

    private final GameStateReceiver gameStateReceiver;
    private final Arena arena;
    private final InputManager inputManager;
    private final Hud hud;
    private final SpriteBatch spriteBatch;
    private GameStateMessage currentGameState;


    public GameScreen(Game game) {
        gameStateReceiver = new GameStateReceiver();
        inputManager = new InputManager();

        spriteBatch = new SpriteBatch();
        arena = new Arena();
        hud = new Hud(spriteBatch);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        // clear the screen
        Gdx.gl.glClearColor(192 / 255f, 192 / 255f, 192 / 255f, 1); // to get that win 95 look
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        inputManager.handleMovementInput();
        inputManager.handleShootingInput();

        currentGameState = gameStateReceiver.getLatestGameStateMessage();
        arena.update(currentGameState);  // update players' and bullets' positions
        hud.update(currentGameState);  // update info overlay with names, lives etc

        // all rendering should happen between spriteBatch.begin() and spriteBatch.end()
        spriteBatch.begin();
        arena.render(delta, spriteBatch);  // players, bullets
        spriteBatch.end();
    }
}
