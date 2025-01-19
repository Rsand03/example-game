package ee.taltech.examplegame.screen.overlay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import ee.taltech.examplegame.game.Arena;
import ee.taltech.examplegame.network.ServerConnection;
import message.GameStateMessage;
import message.PlayerState;

import static constant.Constants.PLAYER_LIVES_COUNT;
import static ee.taltech.examplegame.component.TextLabel.createLabel;




public class Hud {

    public static final int LABEL_SIZE = 20;
    private final Integer ROW_PADDING_TOP = 20;

    private final Integer localPLayerId = ServerConnection.getInstance().getClient().getID();

    private Stage stage;
    private Arena activeArena;

    private final Label localPlayerNameLabel = createLabel("You", Color.GREEN, LABEL_SIZE);
    private final Label timeLabel = createLabel("0:00", Color.WHITE, LABEL_SIZE);
    private final Label remotePlayerNameLabel = createLabel("Enemy", Color.WHITE, LABEL_SIZE);

    private final Label localPlayerLivesLabel = createLabel(String.valueOf(PLAYER_LIVES_COUNT), Color.RED, LABEL_SIZE);
    private final Label placeholderLabel = createLabel("", Color.WHITE, LABEL_SIZE);
    private final Label remotePlayerLivesLabel = createLabel(String.valueOf(PLAYER_LIVES_COUNT), Color.RED, LABEL_SIZE);


    public Hud(Arena arena) {

        activeArena = arena;

        Viewport viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        stage = new Stage(viewport);

        Table table = new Table();

        table.setFillParent(true);
        table.top();  // top-align

        // first row
        table.add(localPlayerNameLabel).expandX().padTop(ROW_PADDING_TOP);
        table.add(timeLabel).expandX().padTop(ROW_PADDING_TOP);
        table.add(remotePlayerNameLabel).expandX().padTop(ROW_PADDING_TOP);

        // second row
        table.row();
        table.add(localPlayerLivesLabel).expandX();
        table.add(placeholderLabel).expandX();
        table.add(remotePlayerLivesLabel).expandX();

        table.setDebug(false);  // setting to true will outline all table cells with a red line

        stage.addActor(table);
    }

    public void render(SpriteBatch spriteBatch) {
        if (activeArena.getLatestGameStateMessage() == null) return;

        GameStateMessage gameState = activeArena.getLatestGameStateMessage();
        for (PlayerState player : gameState.getPlayerStates()) {
            if (player.getId() == localPLayerId) {
                localPlayerLivesLabel.setText(player.getLives());
            } else {
                remotePlayerLivesLabel.setText(player.getLives());
            }
        }

        // stage must be drawn (rendered) during each frame, even if there are no new changes
        spriteBatch.setProjectionMatrix(stage.getCamera().combined);
        stage.draw();
    }

}
