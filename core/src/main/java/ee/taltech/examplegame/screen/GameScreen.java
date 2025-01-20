package ee.taltech.examplegame.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import ee.taltech.examplegame.game.Arena;
import ee.taltech.examplegame.game.GameStateManager;
import ee.taltech.examplegame.game.PlayerInputManager;
import ee.taltech.examplegame.screen.overlay.Hud;
import message.GameStateMessage;

public class GameScreen extends ScreenAdapter {

    private final Game game;
    private final GameStateManager gameStateManager;
    private final PlayerInputManager playerInputManager;

    private final SpriteBatch spriteBatch;
    private final Arena arena;
    private final Hud hud;

    /**
     * Initialize new GameScreen, where the main gameplay happens.
     *
     * @param game Current game instance, used for navigating between screens, for example GameScreen -> TitleScreen
     */
    public GameScreen(Game game) {
        this.game = game;
        gameStateManager = new GameStateManager();  // receives game state from the server
        playerInputManager = new PlayerInputManager();  // listens for user input and sends it to the server

        spriteBatch = new SpriteBatch();  // where all the content is rendered
        arena = new Arena();  // manages players and bullets
        hud = new Hud(spriteBatch);  // info overlay
    }

    /**
     * Render a new frame.
     *
     * @param delta time since rendering the previous frame
     */
    @Override
    public void render(float delta) {
        super.render(delta);
        // clear the screen
        Gdx.gl.glClearColor(192 / 255f, 192 / 255f, 192 / 255f, 1); // to get that win 95 look
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        playerInputManager.handleMovementInput(); // send player movement info to server
        playerInputManager.handleShootingInput(); // send player shooting info to server
        handleScreenNavigation(game); // ESC - navigate back to Title screen

        GameStateMessage currentGameState = gameStateManager.getLatestGameStateMessage();
        arena.update(currentGameState);  // update players' and bullets' positions
        hud.update(currentGameState);  // update info overlay time, lives etc

        // render players and bullets onto the screen
        spriteBatch.begin();
        arena.render(delta, spriteBatch);  // all spriteBatch rendering should occur between .begin() and .end()
        spriteBatch.end();

        hud.render(); // render the HUD last to ensure it appears on top of all other content
    }

    public void handleScreenNavigation(Game game) {
        // navigate to TitleScreen
        if (Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            game.setScreen(new TitleScreen(game));
        }
    }
}
