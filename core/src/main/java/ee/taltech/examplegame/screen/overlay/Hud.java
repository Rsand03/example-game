package ee.taltech.examplegame.screen.overlay;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import ee.taltech.examplegame.game.Arena;
import ee.taltech.examplegame.network.ServerConnection;
import message.GameStateMessage;
import message.PlayerState;

import java.util.List;

import static constant.Constants.PLAYER_LIVES_COUNT;
import static ee.taltech.examplegame.component.TextLabel.createLabel;


public class Hud {

    private static final int LABEL_SIZE = 20;
    private static final Integer TABLE_PADDING_TOP = 20;
    public static final int GAME_STATUS_LABEL_PADDING_TOP = 175;

    private final Arena activeArena;
    private final Stage stage;
    private final Integer localPLayerId;

    // initialize HUD labels with placeholder values, which can later be updated as the game state changes
    private final Label localPlayerNameLabel = createLabel("You", Color.GREEN, LABEL_SIZE);
    private final Label timeLabel = createLabel("0:00", Color.WHITE, LABEL_SIZE);
    private final Label remotePlayerNameLabel = createLabel("Enemy", Color.WHITE, LABEL_SIZE);

    private final Label localPlayerLivesLabel = createLabel(String.valueOf(PLAYER_LIVES_COUNT), Color.RED, LABEL_SIZE);
    private final Label remotePlayerLivesLabel = createLabel(String.valueOf(PLAYER_LIVES_COUNT), Color.RED, LABEL_SIZE);

    private final Label gameStatusLabel = createLabel("Waiting for other player...", Color.WHITE, LABEL_SIZE);


    public Hud(Arena arena) {
        activeArena = arena;
        localPLayerId = ServerConnection.getInstance().getClient().getID();

        // viewport with hardcoded width and height works with any screen/window size
        // there is also no need for additional font re-scaling when adjusting window size
        Viewport viewport = new FitViewport(640, 480, new OrthographicCamera());

        // create a stage to render the HUD content
        stage = new Stage(viewport);

        // create a table to display fields such as lives count
        Table table = createHudTable();
        table.setDebug(false);  // true - outline all table cells, labels with a red line (makes table non-transparent)
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

        // third row
        table.add(gameStatusLabel)
            .colspan(3)  // make the label span across all 3 table columns
            .padTop(GAME_STATUS_LABEL_PADDING_TOP)
            .align(Align.center);  // center-align the label within the cell
        ;
        table.row();

        return table;
    }

    public void render() {
        GameStateMessage gameState = activeArena.getLatestGameStateMessage();
        if (gameState == null) return;

        updateLives(gameState.getPlayerStates());
        updateTime(gameState.getGameTime());

        updateGameStatus(gameState);

        // stage must be drawn (rendered) during each frame, even if there are no changes
        stage.draw();
    }

    private void updateLives(List<PlayerState> players) {
        for (PlayerState player : players) {
            if (player.getId() == localPLayerId) {
                localPlayerLivesLabel.setText(player.getLives());
            } else {
                remotePlayerLivesLabel.setText(player.getLives());
            }
        }
    }

    private void updateTime(int gameTime) {
        int minutes = Math.floorDiv(gameTime, 60);
        int seconds = gameTime % 60;
        timeLabel.setText(minutes + ":" + String.format("%02d", seconds));
    }

    private void updateGameStatus(GameStateMessage gameState) {
        if (gameState.isAllPlayersHaveJoined()) {
            gameStatusLabel.setText("");  // remove "waiting for other players..." message
        }
        for (PlayerState player : gameState.getPlayerStates()) {
            if (player.getId() == localPLayerId && player.getLives() == 0) {
                gameStatusLabel.setColor(Color.RED);
                gameStatusLabel.setText("You lost");
            } else if (player.getId() != localPLayerId && player.getLives() == 0) {
                gameStatusLabel.setColor(Color.GREEN);
                gameStatusLabel.setText("You won");
            }
        }
    }

}
