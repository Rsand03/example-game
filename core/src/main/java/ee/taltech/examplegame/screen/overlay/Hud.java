package ee.taltech.examplegame.screen.overlay;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
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

    private static final int LABEL_SIZE = 20;
    private static final Integer TABLE_PADDING_TOP = 20;

    private final Arena activeArena;
    private final Stage stage;
    private final Integer localPLayerId;

    // initialize HUD labels with placeholder values, which can later be updated as the game state changes
    private final Label localPlayerNameLabel = createLabel("You", Color.GREEN, LABEL_SIZE);
    private final Label timeLabel = createLabel("0:00", Color.WHITE, LABEL_SIZE);
    private final Label remotePlayerNameLabel = createLabel("Enemy", Color.WHITE, LABEL_SIZE);

    private final Label localPlayerLivesLabel = createLabel(String.valueOf(PLAYER_LIVES_COUNT), Color.RED, LABEL_SIZE);
    private final Label remotePlayerLivesLabel = createLabel(String.valueOf(PLAYER_LIVES_COUNT), Color.RED, LABEL_SIZE);


    public Hud(Arena arena) {
        activeArena = arena;
        localPLayerId = ServerConnection.getInstance().getClient().getID();

        // create a stage to render the HUD content
        Viewport viewport = new FitViewport(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), new OrthographicCamera());
        stage = new Stage(viewport);

        // create a table to display fields such as lives count
        Table table = createHudTable();
        table.setDebug(false);  // setting this to true will outline all table cells, labels with a red line
        stage.addActor(table);
    }

    private Table createHudTable() {
        // in very simple tables, using an empty placeholder label to adjust alignment is usually the easiest way
        Label emptyLabel = createLabel("", Color.WHITE, LABEL_SIZE);

        Table table = new Table();

        table.setFillParent(true);
        table.top();  // align the table's content to the top
        table.padTop(TABLE_PADDING_TOP);

        // first row
        table.add(localPlayerNameLabel);
        table.add(timeLabel);
        table.add(remotePlayerNameLabel);
        table.row().expandX();  // make the row fill the entire width of the screen

        // second row
        table.add(localPlayerLivesLabel);
        table.add(emptyLabel);  // empty label as a placeholder for alignment
        table.add(remotePlayerLivesLabel);
        table.row().expandX();
        return table;
    }

    public void render() {
        if (activeArena.getLatestGameStateMessage() == null) return;

        GameStateMessage gameState = activeArena.getLatestGameStateMessage();
        for (PlayerState player : gameState.getPlayerStates()) {
            if (player.getId() == localPLayerId) {
                localPlayerLivesLabel.setText(player.getLives());
            } else {
                remotePlayerLivesLabel.setText(player.getLives());
            }
        }

        // stage must be drawn (rendered) during each frame, even if there are no changes
        stage.draw();
    }

}
